package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.repository.DebtRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.DebtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DebtServiceImp implements DebtService {

    private static final Logger logger = LoggerFactory.getLogger(DebtServiceImp.class);

    private final DebtRepository debtRepository;
    private final AuthenticationService authenticationService;

    public DebtServiceImp(DebtRepository debtRepository, AuthenticationService authenticationService) {
        this.debtRepository = debtRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Debt> getAllDebts() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all debts related to user with id {}", currentUserId);
        return debtRepository.findAllByCreditorIdOrBorrowerId(currentUserId, currentUserId);
    }

    @Override
    public List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);

        logger.info("Fetching debts page related to user with id {}", currentUserId);

        return debtRepository.findAllByCreditorIdOrBorrowerId(currentUserId, currentUserId, pageable);
    }

    @Override
    public Debt getDebt(Long id) {
        logger.info("Fetching debt with id {}", id);
        final Optional<Debt> debt = debtRepository.findById(id);

        if (debt.isEmpty()) {
            logger.error("Debt with id {} not founded", id);
            throw new ResourceNotFoundException("Debt", "id", id);
        }

        return debt.get();
    }

    @Override
    public Debt getDebtWithUsers(Long userId1, Long userId2) {
        logger.info("Fetching debt between users {} and {}", userId1, userId2);

        Optional<Debt> debt = debtRepository.findByCreditorIdAndBorrowerId(userId1, userId2);

        if (debt.isPresent()) {
            return debt.get();
        }

        debt = debtRepository.findByCreditorIdAndBorrowerId(userId2, userId1);

        if (debt.isPresent()) {
            return debt.get();
        }

        logger.error("Debt between users {} and {} not founded", userId1, userId2);
        throw new ResourceNotFoundException("Debt", "creditorId and borrowerId", userId1 + " and " + userId2);
    }

    @Override
    public Debt getDebtWithUserAndCurrentUser(Long userId) {
        return getDebtWithUsers(authenticationService.getCurrentUser().getId(), userId);
    }

    @Override
    public Boolean isExistDebtWithUsers(Long userId1, Long userId2) {
        return debtRepository.existsByCreditorIdAndBorrowerId(userId1, userId2)
                || debtRepository.existsByCreditorIdAndBorrowerId(userId2, userId1);
    }

    @Override
    public Boolean isExistDebtWithUserAndCurrentUser(Long userId) {
        return isExistDebtWithUsers(authenticationService.getCurrentUser().getId(), userId);
    }

    @Override
    public Debt createDebt(Debt debt) {
        logger.info("Creating debt: {}", debt);
        return debtRepository.save(debt);
    }

    @Override
    public Debt updateDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    @Override
    public void deleteDebt(Long id) {
        logger.info("Deleting debt with id {}", id);
        debtRepository.deleteById(id);
    }

    @Override
    public Debt addToBalance(Long debtId, Float cash) {
        logger.info("Adding {} to balance of debt with id {}", cash, debtId);

        final Debt debt = getDebt(debtId);
        debt.setBalance(debt.getBalance() + cash);
        return debtRepository.save(debt);
    }

    @Override
    public Boolean isBalanceZero(Debt debt) {
        return Math.abs(debt.getBalance()) < 0.005;
    }

}
