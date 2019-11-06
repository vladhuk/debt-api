package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Debt;

import java.util.List;

public interface DebtService {

    List<Debt> getAllDebts();

    List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize);

    Boolean isExistsDebtWithUser(Long userId);

}
