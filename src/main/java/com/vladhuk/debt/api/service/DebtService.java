package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Debt;

import java.util.List;

public interface DebtService {

    List<Debt> getAllDebts();

    List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize);

    Debt getDebt(Long id);

    Debt getDebtWithUsers(Long userId1, Long userId2);

    Boolean isExistDebtWithUsers(Long userId1, Long userId2);

    Boolean isExistDebtWithUser(Long userId);

    Debt createDebt(Debt debt);

    Debt updateDebt(Debt debt);

    void deleteDebt(Long id);

    Debt addToBalance(Long debtId, Float cash);

    Boolean isBalanceZero(Debt debt);

}
