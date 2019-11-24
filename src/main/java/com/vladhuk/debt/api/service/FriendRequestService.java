package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.FriendRequest;

import java.util.List;

public interface FriendRequestService {

    List<FriendRequest> getAllSentFriendRequests();

    List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize);

    /**
     * If user didn't view request before, request status is changed to VIEWED.
     */
    List<FriendRequest> changeStatusToViewed(List<FriendRequest> requests);

    List<FriendRequest> getAllReceivedFriendRequests();

    List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize);

    Long countNewReceivedFriendRequests();

    void deleteSentFriendRequestIfNotConfirmedOrRejected(Long requestId);

    FriendRequest sendFriendRequest(FriendRequest friendRequest);

    FriendRequest confirmFriendRequestAndDeleteSameViewed(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

}
