package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.model.User;

import java.util.List;

public interface UserService {

    List<User> getFriends(Integer pageNumber, Integer pageSize);

    Boolean deleteFriend(Long friendId);

    List<FriendRequest> getSentFriendRequests(Integer pageNumber, Integer pageSize);

    List<FriendRequest> getReceivedFriendRequests(Integer pageNumber, Integer pageSize);

    FriendRequest sendFriendRequest(FriendRequest friendRequest);

    FriendRequest acceptFriendRequest(Long requestId);

    FriendRequest rejectFriendRequest(Long requestId);

    List<User> getBlacklist(Integer pageNumber, Integer pageSize);

    List<User> addUserToBlacklist(User user);

    void deleteUserFromBlacklist(Long userId);

}
