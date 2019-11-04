package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.FriendService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendServiceImpl implements FriendService {
    @Override
    public List<User> getAllFriends() {
        return null;
    }

    @Override
    public List<User> getFriendsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Boolean deleteFriend(Long friendId) {
        return null;
    }
}
