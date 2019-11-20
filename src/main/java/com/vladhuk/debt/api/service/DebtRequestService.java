package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.DebtRequest;

import java.util.List;

public interface DebtRequestService {

    List<DebtRequest> getAllSentDebtRequests();

    List<DebtRequest> getSentDebtRequestsPage(Integer pageNumber, Integer pageSize);

    /**
     * If user didn't view request before, order status is changed to VIEWED.
     */
    List<DebtRequest> changeOrderStatusToViewed(List<DebtRequest> requests);

    void changeStatusToConfirmedIfAllOrdersConfirmed(DebtRequest debtRequest);

    List<DebtRequest> getAllReceivedDebtRequests();

    List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedDebtRequests();

    void deleteSentDebtRequestIfNotConfirmedOrRejected(Long requestId);

    DebtRequest sendDebtRequest(DebtRequest debtRequest);

    DebtRequest confirmDebtRequestAndUpdateBalance(Long requestId);

    DebtRequest rejectDebtRequest(Long requestId);

}
