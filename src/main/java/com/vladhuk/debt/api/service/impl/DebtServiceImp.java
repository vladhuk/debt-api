package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.repository.DebtRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.DebtService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DebtServiceImp implements DebtService {

    private final DebtRepository debtRepository;
    private final AuthenticationService authenticationService;

    public DebtServiceImp(DebtRepository debtRepository, AuthenticationService authenticationService) {
        this.debtRepository = debtRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public List<Debt> getAllDebts() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        return debtRepository.findAllByCreditorIdOrBorrowerId(currentUserId, currentUserId);
    }

    @Override
    public List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return debtRepository.findAllByCreditorIdOrBorrowerId(currentUserId, currentUserId, pageable);
    }

    // TODO: Make optional
    @Override
    public Debt getDebt(Long id) {
        return debtRepository.findById(id).orElse(null);
    }

    @Override
    public Boolean isExistsDebtWithUser(Long userId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();

        return debtRepository.existsByCreditorIdAndBorrowerId(currentUserId, userId)
                || debtRepository.existsByCreditorIdAndBorrowerId(userId, currentUserId);
    }

    @Override
    public Debt createDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    @Override
    public void deleteDebt(Long id) {
        debtRepository.deleteById(id);
    }

    @Override
    public Debt addToBalance(Long debtId, Float cash) {
        final Debt debt = getDebt(debtId);
        debt.setBalance(debt.getBalance() + cash);
        return debtRepository.save(debt);
    }

    @Override
    public Boolean isBalanceZero(Debt debt) {
        return Math.abs(debt.getBalance()) < 0.005;
    }

}
