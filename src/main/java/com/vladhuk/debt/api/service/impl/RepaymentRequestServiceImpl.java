package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.RepaymentRequest;
import com.vladhuk.debt.api.service.RepaymentRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RepaymentRequestServiceImpl implements RepaymentRequestService {
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
    public Long countNewReceivedRepaymentRequests() {
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
