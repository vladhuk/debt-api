package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.RepaymentRequest;

import java.util.List;

public interface RepaymentRequestService {

    List<RepaymentRequest> getAllSentRepaymentRequests();

    List<RepaymentRequest> getSentRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    List<RepaymentRequest> getAllReceivedRepaymentRequests();

    List<RepaymentRequest> getReceivedRepaymentRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedRepaymentRequests();

    RepaymentRequest sendRepaymentRequest(RepaymentRequest repaymentRequest);

    RepaymentRequest acceptRepaymentRequest(Long requestId);

    RepaymentRequest rejectRepaymentRequest(Long requestId);

}
