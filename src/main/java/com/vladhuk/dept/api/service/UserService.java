package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllFriends();

    List<User> getFriendsPage(Integer pageNumber, Integer pageSize);

    Boolean deleteFriend(Long friendId);

    List<FriendRequest> getAllSentFriendRequests();

    List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize);

    List<FriendRequest> getAllReceivedFriendRequests();

    List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize);

    FriendRequest sendFriendRequest(FriendRequest friendRequest);

    FriendRequest acceptFriendRequest(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

    List<User> getFullBlacklist();

    List<User> getBlacklistPage(Integer pageNumber, Integer pageSize);

    List<User> addUserToBlacklist(User user);

    void deleteUserFromBlacklist(Long userId);

}
