package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.DebtRequest;

import java.util.List;

public interface DebtRequestService {

    List<DebtRequest> getAllSentDebtRequests();

    List<DebtRequest> getSentDebtRequestsPage(Integer pageNumber, Integer pageSize);

    List<DebtRequest> getAllReceivedDebtRequests();

    List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedDebtRequests();

    DebtRequest sendDebtRequest(DebtRequest debtRequest);

    DebtRequest acceptDebtRequest(Long requestId);

    DebtRequest rejectDebtRequest(Long requestId);

}
