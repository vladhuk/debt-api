package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Debt;

import java.util.List;

public interface DebtService {

    List<Debt> getAllDebts();

    List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize);

}
