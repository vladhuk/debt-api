package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.service.DebtService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtServiceImp implements DebtService {

    @Override
    public List<Debt> getAllDebts() {
        return null;
    }

    @Override
    public List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }
}