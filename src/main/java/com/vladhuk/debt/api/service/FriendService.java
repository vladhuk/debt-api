package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.User;

import java.util.List;

public interface FriendService {

    List<User> getAllFriends();

    List<User> getFriendsPage(Integer pageNumber, Integer pageSize);

    Boolean deleteFriend(Long friendId);

}
