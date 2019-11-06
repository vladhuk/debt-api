package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.FriendRequest;

import java.util.List;

public interface FriendRequestService {

    List<FriendRequest> getAllSentFriendRequests();

    List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize);

    List<FriendRequest> getAllReceivedFriendRequests();

    List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedFriendRequests();

    FriendRequest sendFriendRequest(FriendRequest friendRequest);

    FriendRequest acceptFriendRequest(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

}
