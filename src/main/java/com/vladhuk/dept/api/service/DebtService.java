package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.model.RepaymentRequest;

import java.util.List;

public interface DebtService {

    List<Debt> getDebts(Integer pageNumber, Integer pageSize);

    List<DebtRequest> getSentDebtRequests(Integer pageNumber, Integer pageSize);

    List<DebtRequest> getReceivedDebtRequests(Integer pageNumber, Integer pageSize);

    DebtRequest sendDebtRequest(DebtRequest debtRequest);

    DebtRequest acceptDebtRequest(Long requestId);

    DebtRequest rejectDebtRequest(Long requestId);

    List<RepaymentRequest> getSentRepaymentRequests(Integer pageNumber, Integer pageSize);

    List<RepaymentRequest> getReceivedRepaymentRequests(Integer pageNumber, Integer pageSize);

    RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest);

    RepaymentRequest acceptRepaymentRequest(Long requestId);

    RepaymentRequest rejectRepaymentRequest(Long requestId);

}
