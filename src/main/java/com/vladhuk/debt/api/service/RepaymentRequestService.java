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

    void deleteSentRepaymentRequestIfNotAcceptedOrRejected(Long requestId);

    RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest);

    RepaymentRequest acceptRepaymentRequestAndUpdateBalance(Long requestId);

    RepaymentRequest rejectRepaymentRequest(Long requestId);

    List<RepaymentRequest> rejectRepaymentRequestsWithUsersIfStatusSentOrViewed(Long firstUserId, Long secondUserId);

}
