package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.RepaymentRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.*;
import com.vladhuk.debt.api.repository.RepaymentOrderRepository;
import com.vladhuk.debt.api.repository.RepaymentRequestRepository;
import com.vladhuk.debt.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.vladhuk.debt.api.model.Status.StatusName.*;

@Service
@Transactional
public class RepaymentRequestServiceImpl implements RepaymentRequestService {

    private static final Logger logger = LoggerFactory.getLogger(RepaymentRequestServiceImpl.class);

    private final RepaymentRequestRepository repaymentRequestRepository;
    private final RepaymentOrderRepository repaymentOrderRepository;
    private final AuthenticationService authenticationService;
    private final DebtService debtService;
    private final UserService userService;
    private final StatusService statusService;

    public RepaymentRequestServiceImpl(RepaymentRequestRepository repaymentRequestRepository, RepaymentOrderRepository repaymentOrderRepository, AuthenticationService authenticationService, DebtService debtService, UserService userService, StatusService statusService) {
        this.repaymentRequestRepository = repaymentRequestRepository;
        this.repaymentOrderRepository = repaymentOrderRepository;
        this.authenticationService = authenticationService;
        this.debtService = debtService;
        this.userService = userService;
        this.statusService = statusService;
    }

    @Override
    public List<RepaymentRequest> getAllSentRepaymentRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all sent from user with id {} repayment requests", currentUserId);
        return repaymentRequestRepository.findAllBySenderId(currentUserId);
    }

    @Override
    public List<RepaymentRequest> getSentRepaymentRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching sent from user with id {} debt repayment page", currentUserId);

        return repaymentRequestRepository.findAllBySenderId(currentUserId, pageable);
    }

    @Override
    public List<RepaymentRequest> changeStatusToViewed(List<RepaymentRequest> requests) {
        final Status viewedStatus = statusService.getStatus(VIEWED);

        requests.stream()
                .filter(request -> request.getStatus().getName() == SENT)
                .forEach(request -> {
                    request.setStatus(viewedStatus);
                    repaymentRequestRepository.save(request);
                });

        return requests;
    }

    @Override
    public List<RepaymentRequest> getAllReceivedRepaymentRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all received by user with id {} repayment requests", currentUserId);
        return changeStatusToViewed(repaymentRequestRepository.findAllByOrderReceiverId(currentUserId));
    }

    @Override
    public List<RepaymentRequest> getReceivedRepaymentRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching received by user with id {} repayment requests page", currentUserId);

        return changeStatusToViewed(repaymentRequestRepository.findAllByOrderReceiverId(currentUserId, pageable));
    }

    @Override
    public Long countNewReceivedRepaymentRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long statusId = statusService.getStatus(SENT).getId();

        logger.info("Counting new received by user with id {} repayment requests", currentUserId);

        return repaymentRequestRepository.countAllByOrderReceiverIdAndStatusId(currentUserId, statusId);
    }

    @Override
    public RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest) {
        final User currentUser = authenticationService.getCurrentUser();
        final User receiver = userService.getUser(repaymentRequest.getOrder().getReceiver());

        logger.info("Sending debt request from user {} to user {}", currentUser.getId(), receiver.getId());

        if (!debtService.isExistDebtWithUserAndCurrentUser(receiver.getId())) {
            logger.error("User {} can not repayment debt request to user with id {}, because they don't have debt", currentUser.getId(), receiver.getId());
            throw new RepaymentRequestException("Can not send request because users don't have debt");
        }

        final Status sentStatus = statusService.getStatus(SENT);

        final Order order = new Order(repaymentRequest.getOrder().getAmount(), sentStatus, receiver);
        repaymentOrderRepository.save(order);

        final RepaymentRequest requestForSave = new RepaymentRequest();
        requestForSave.setSender(currentUser);
        requestForSave.setOrder(order);
        requestForSave.setComment(repaymentRequest.getComment());
        requestForSave.setStatus(sentStatus);

        return repaymentRequestRepository.save(requestForSave);
    }

    private RepaymentRequest getViewedRepaymentRequest(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Optional<RepaymentRequest> request = repaymentRequestRepository.findByIdAndOrderReceiverIdAndStatusId(
                requestId, currentUserId, statusService.getStatus(VIEWED).getId()
        );

        if (request.isEmpty()) {
            logger.error("Repayment request with id {} and status VIEWED with receiverId {} not founded", requestId, currentUserId);
            throw new ResourceNotFoundException("Repayment request with status VIEWED", "receiverId", currentUserId);
        }

        return request.get();
    }

    @Override
    public RepaymentRequest confirmRepaymentRequestAndUpdateBalance(Long requestId) {
        logger.info("Confirming repayment request with id {}", requestId);

        final RepaymentRequest request = getViewedRepaymentRequest(requestId);


        request.setStatus(statusService.getStatus(CONFIRMED));

        final Debt debt = debtService.getDebtWithUserAndCurrentUser(request.getSender().getId());
        final Float amount = Objects.equals(debt.getCreditor().getId(), request.getSender().getId())
                ? request.getOrder().getAmount()
                : -request.getOrder().getAmount();
        debtService.addToBalance(debt.getId(), amount);

        if (debtService.isBalanceZero(debt)) {
            debtService.deleteDebt(debt.getId());
        }

        return repaymentRequestRepository.save(request);
    }

    @Override
    public RepaymentRequest rejectRepaymentRequest(Long requestId) {
        logger.info("Rejecting repayment request with id {}", requestId);

        final Status rejectedStatus = statusService.getStatus(REJECTED);

        final RepaymentRequest request = getViewedRepaymentRequest(requestId);
        request.setStatus(rejectedStatus);

        return repaymentRequestRepository.save(request);
    }
}
