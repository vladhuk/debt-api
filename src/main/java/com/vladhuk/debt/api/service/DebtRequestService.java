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

    void changeStatusToAcceptedIfAllOrdersAccepted(DebtRequest debtRequest);

    List<DebtRequest> getAllReceivedDebtRequests();

    List<DebtRequest> getReceivedDebtRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedDebtRequests();

    void deleteSentDebtRequestIfNotAcceptedOrRejected(Long requestId);

    DebtRequest sendDebtRequest(DebtRequest debtRequest);

    DebtRequest acceptDebtRequestAndUpdateBalance(Long requestId);

    DebtRequest rejectDebtRequest(Long requestId);

}
