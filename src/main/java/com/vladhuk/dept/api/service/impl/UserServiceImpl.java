package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public List<User> getFriends(Integer pageNumber, Integer pageSize) {
        return null;
    }
    @Override
    public Boolean deleteFriend(Long friendId) {
        return null;
    }
    @Override
    public List<FriendRequest> getSentFriendRequests(Integer pageNumber, Integer pageSize) {
        return null;
    }
    @Override
    public List<FriendRequest> getReceivedFriendRequests(Integer pageNumber, Integer pageSize) {
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
    @Override
    public List<User> getBlacklist(Integer pageNumber, Integer pageSize) {
        return null;
    }
    @Override
    public List<User> addUserToBlacklist(User user) {
        return null;
    }
    @Override
    public void deleteUserFromBlacklist(Long userId) {

    }
}
