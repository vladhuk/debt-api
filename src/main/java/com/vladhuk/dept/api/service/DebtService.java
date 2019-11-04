package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Debt;
import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.model.RepaymentRequest;

import java.util.List;

public interface DebtService {

    List<Debt> getAllDebts();

    List<Debt> getDebtsPage(Integer pageNumber, Integer pageSize);

    List<DebtRequest> getAllSentDebtRequests();

    List<DebtRequest> getSentDebtRequestsPage(Integer pageNumber, Integer pageSize);

    List<DebtRequest> getAllReceivedDebtRequests();

    List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize);

    DebtRequest sendDebtRequest(DebtRequest debtRequest);

    DebtRequest acceptDebtRequest(Long requestId);

    DebtRequest rejectDebtRequest(Long requestId);

    List<RepaymentRequest> getAllSentRepaymentRequests();

    List<RepaymentRequest> getSentRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    List<RepaymentRequest> getAllReceivedRepaymentRequests();

    List<RepaymentRequest> getReceivedRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest);

    RepaymentRequest acceptRepaymentRequest(Long requestId);

    RepaymentRequest rejectRepaymentRequest(Long requestId);

}
