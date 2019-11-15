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
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching debt with id {} related to user with id {}", id, currentUserId);
        final Optional<Debt> debt = debtRepository.findByCreditorIdOrBorrowerId(currentUserId, currentUserId);

        if (debt.isEmpty()) {
            logger.error("Debt with id {} related to user with id {} not founded", id, currentUserId);
            throw new ResourceNotFoundException("Debt", "id", id);
        }

        return debt.get();
    }

    @Override
    public Boolean isExistsDebtWithUser(Long userId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();

        return debtRepository.existsByCreditorIdAndBorrowerId(currentUserId, userId)
                || debtRepository.existsByCreditorIdAndBorrowerId(userId, currentUserId);
    }

    @Override
    public Debt createDebt(Debt debt) {
        logger.info("Creating debt: {}", debt);
        return debtRepository.save(debt);
    }

    @Override
    public void deleteDebt(Long id) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Deleting debt with id {} related to user with id {}", id, currentUserId);
        debtRepository.deleteById_And_CreditorOrBorrowerId(id, currentUserId);
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
