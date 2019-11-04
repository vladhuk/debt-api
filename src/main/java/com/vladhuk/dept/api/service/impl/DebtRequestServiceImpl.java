package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.DebtRequest;
import com.vladhuk.dept.api.service.DebtRequestService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DebtRequestServiceImpl implements DebtRequestService {
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
}
