package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.User;

import java.util.List;

public interface FriendService {

    List<User> getAllFriends();

    List<User> getFriendsPage(Integer pageNumber, Integer pageSize);

    void deleteFriendship(Long friendId);

    User createFriendship(Long friendId);

    Boolean isFriend(Long userId);

}
