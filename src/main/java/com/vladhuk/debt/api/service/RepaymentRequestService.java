package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.RepaymentRequest;

import java.util.List;

public interface RepaymentRequestService {

    List<RepaymentRequest> getAllSentRepaymentRequests();

    List<RepaymentRequest> getSentRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    /**
     * If user didn't view request before, status is changed to VIEWED.
     */
    List<RepaymentRequest> changeStatusToViewed(List<RepaymentRequest> requests);

    List<RepaymentRequest> getAllReceivedRepaymentRequests();

    List<RepaymentRequest> getReceivedRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedRepaymentRequests();

    RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest);

    RepaymentRequest confirmRepaymentRequestAndUpdateBalance(Long requestId);

    RepaymentRequest rejectRepaymentRequest(Long requestId);

}
