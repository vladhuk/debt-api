package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.service.FriendRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {
    @Override
    public List<FriendRequest> getAllSentFriendRequests() {
        return null;
    }

    @Override
    public List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public List<FriendRequest> getAllReceivedFriendRequests() {
        return null;
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Long countNewReceivedFriendRequests() {
        return null;
    }

    @Override
    public FriendRequest sendFriendRequest(FriendRequest friendRequest) {
        return null;
    }

    @Override
    public FriendRequest acceptFriendRequest(Long requestId) {
        return null;
    }

    @Override
    public FriendRequest rejectFriendRequest(Long requestId) {
        return null;
    }
}
