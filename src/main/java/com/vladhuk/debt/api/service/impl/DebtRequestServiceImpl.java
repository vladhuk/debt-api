package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.exception.UserNotFriendException;
import com.vladhuk.debt.api.model.*;
import com.vladhuk.debt.api.repository.DebtOrderRepository;
import com.vladhuk.debt.api.repository.DebtRequestRepository;
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
import java.util.stream.Collectors;

import static com.vladhuk.debt.api.model.Status.StatusName.*;

@Service
@Transactional
public class DebtRequestServiceImpl implements DebtRequestService {

    private static final Logger logger = LoggerFactory.getLogger(DebtRequestRepository.class);

    private final DebtRequestRepository debtRequestRepository;
    private final DebtOrderRepository debtOrderRepository;
    private final AuthenticationService authenticationService;
    private final StatusService statusService;
    private final UserService userService;
    private final FriendService friendService;
    private final DebtService debtService;

    public DebtRequestServiceImpl(DebtRequestRepository debtRequestRepository, DebtOrderRepository debtOrderRepository, AuthenticationService authenticationService, StatusService statusService, UserService userService, FriendService friendService, DebtService debtService) {
        this.debtRequestRepository = debtRequestRepository;
        this.debtOrderRepository = debtOrderRepository;
        this.authenticationService = authenticationService;
        this.statusService = statusService;
        this.userService = userService;
        this.friendService = friendService;
        this.debtService = debtService;
    }

    @Override
    public List<DebtRequest> getAllSentDebtRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all sent from user with id {} debt requests", currentUserId);
        return debtRequestRepository.findAllBySenderId(currentUserId);
    }

    @Override
    public List<DebtRequest> getSentDebtRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching sent from user with id {} debt requests page", currentUserId);

        return debtRequestRepository.findAllBySenderId(currentUserId, pageable);
    }

    @Override
    public List<DebtRequest> changeOrderStatusToViewed(List<DebtRequest> requests) {
        final Status viewedStatus = statusService.getStatus(VIEWED);

        requests.forEach(request ->
                request.getOrders()
                        .stream()
                        .filter(order -> order.getStatus().getName() == SENT)
                        .forEach(order -> {
                            order.setStatus(viewedStatus);
                            debtOrderRepository.save(order);
                        })
        );
        return requests;
    }

    @Override
    public List<DebtRequest> getAllReceivedDebtRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all received by user with id {} debt requests", currentUserId);
        return changeOrderStatusToViewed(debtRequestRepository.findAllByReceiverId(currentUserId));
    }

    @Override
    public List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching received by user with id {} debt requests page", currentUserId);

        return changeOrderStatusToViewed(debtRequestRepository.findAllByReceiverId(currentUserId, pageable));
    }

    @Override
    public Long countNewReceivedDebtRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long statusId = statusService.getStatus(SENT).getId();

        logger.info("Counting new received by user with id {} debt requests", currentUserId);

        return debtOrderRepository.countAllByReceiverIdAndStatusId(currentUserId, statusId);
    }

    @Override
    public void deleteSentDebtRequestIfNotAcceptedOrRejected(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Optional<DebtRequest> debtRequest = debtRequestRepository.findByIdAndSenderId(requestId, currentUserId);

        logger.info("Deleting debt request with id {}", requestId);

        if (debtRequest.isEmpty() || debtRequest.get().getStatus().getName() == ACCEPTED
                || debtRequest.get().getStatus().getName() == REJECTED) {
            logger.error("Not rejected or accepted debt request with id {} and sender id {} not founded", requestId, currentUserId);
            throw new ResourceNotFoundException("Not rejected or accepted debt request", "id", requestId);
        }
        debtRequestRepository.deleteById(requestId);
    }

    @Override
    public DebtRequest sendDebtRequest(DebtRequest debtRequest) {
        final User currentUser = authenticationService.getCurrentUser();

        logger.info("Sending debt request from user {} to users {}", currentUser.getId(), debtRequest.getOrders().stream().map(order -> order.getReceiver().getId()).collect(Collectors.toList()));

        final Status sentStatus = statusService.getStatus(SENT);

        final List<Order> orders = debtRequest.getOrders().stream()
                .map(order -> {
                    if (!friendService.isFriend(order.getReceiver().getId())) {
                        logger.error("User {} can not send debt request to user with id {}, because they are not friends", currentUser.getId(), order.getReceiver().getId());
                        throw new UserNotFriendException("Can not send request because users are not friends");
                    }
                    return new Order(order.getAmount(), sentStatus, userService.getUser(order.getReceiver()));
                })
                .collect(Collectors.toList());

        final DebtRequest requestForSave = new DebtRequest();
        requestForSave.setSender(currentUser);
        requestForSave.setOrders(orders);
        requestForSave.setComment(debtRequest.getComment());
        requestForSave.setStatus(sentStatus);

        return debtRequestRepository.save(requestForSave);
    }

    private Order getViewedReceivedOrderByRequest(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long viewedStatusId = statusService.getStatus(VIEWED).getId();
        final Optional<Order> optionalOrder =
                debtOrderRepository.findByDebtRequestIdAndReceiverIdAndStatusId(requestId, currentUserId, viewedStatusId);

        if (optionalOrder.isEmpty()) {
            logger.error("Order in debt request with id {} and status VIEWED with receiverId {} not founded", requestId, currentUserId);
            throw new ResourceNotFoundException("Order in debt request with status VIEWED", "receiverId", currentUserId);
        }

        return optionalOrder.get();
    }

    @Override
    public DebtRequest acceptDebtRequestAndUpdateBalance(Long requestId) {
        logger.info("Acceptance debt request with id {}", requestId);

        final Order order = getViewedReceivedOrderByRequest(requestId);

        order.setStatus(statusService.getStatus(ACCEPTED));
        debtOrderRepository.save(order);
        logger.info("Order with id {} is ACCEPTED", order.getId());

        final DebtRequest debtRequest = debtRequestRepository.findById(requestId).get();

        changeStatusToAcceptedIfAllOrdersAccepted(debtRequest);

        if (debtRequest.getStatus().getName() == ACCEPTED) {
            addToBalances(debtRequest);
        }

        return debtRequestRepository.save(debtRequest);
    }

    @Override
    public void changeStatusToAcceptedIfAllOrdersAccepted(DebtRequest debtRequest) {
        for (Order order : debtRequest.getOrders()) {
            if (order.getStatus().getName() != ACCEPTED) {
                return;
            }
        }
        debtRequest.setStatus(statusService.getStatus(ACCEPTED));
        logger.info("Debt request with id {} is ACCEPTED", debtRequest.getId());
    }

    private void addToBalances(DebtRequest debtRequest) {
        final User sender = debtRequest.getSender();

        for (Order order : debtRequest.getOrders()) {
            final User receiver = order.getReceiver();

            if (!debtService.isExistDebtWithUsers(sender.getId(), receiver.getId())) {
                debtService.createDebt(new Debt(sender, receiver, order.getAmount()));
            } else {
                final Debt debt = debtService.getDebtWithUsers(sender.getId(), receiver.getId());
                final Float amount = Objects.equals(debt.getCreditor().getId(), sender.getId())
                        ? order.getAmount()
                        : -order.getAmount();
                debtService.addToBalance(debt.getId(), amount);
                if (debtService.isBalanceZero(debt)) {
                    debtService.deleteDebt(debt.getId());
                }
            }
        }
    }

    @Override
    public DebtRequest rejectDebtRequest(Long requestId) {
        logger.info("Rejecting debt request with id {}", requestId);

        final Status rejectedStatus = statusService.getStatus(REJECTED);

        final Order order = getViewedReceivedOrderByRequest(requestId);
        order.setStatus(rejectedStatus);
        debtOrderRepository.save(order);

        final DebtRequest debtRequest = debtRequestRepository.findById(requestId).get();
        debtRequest.setStatus(rejectedStatus);

        return debtRequestRepository.save(debtRequest);
    }
}
