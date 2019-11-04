package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.model.RepaymentRequest;
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

    @Override
    public List<DebtRequest> getAllSentDebtRequests() {
        return null;
    }

    @Override
    public List<DebtRequest> getSentDebtRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public List<DebtRequest> getAllReceivedDebtRequests() {
        return null;
    }

    @Override
    public List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public DebtRequest sendDebtRequest(DebtRequest debtRequest) {
        return null;
    }

    @Override
    public DebtRequest acceptDebtRequest(Long requestId) {
        return null;
    }

    @Override
    public DebtRequest rejectDebtRequest(Long requestId) {
        return null;
    }

    @Override
    public List<RepaymentRequest> getAllSentRepaymentRequests() {
        return null;
    }

    @Override
    public List<RepaymentRequest> getSentRepaymentRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public List<RepaymentRequest> getAllReceivedRepaymentRequests() {
        return null;
    }

    @Override
    public List<RepaymentRequest> getReceivedRepaymentRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest) {
        return null;
    }

    @Override
    public RepaymentRequest acceptRepaymentRequest(Long requestId) {
        return null;
    }

    @Override
    public RepaymentRequest rejectRepaymentRequest(Long requestId) {
        return null;
    }
}
