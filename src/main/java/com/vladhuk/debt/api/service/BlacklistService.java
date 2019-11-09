package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.User;

import java.util.List;

public interface BlacklistService {

    List<User> getFullBlacklist();

    List<User> getBlacklistPage(Integer pageNumber, Integer pageSize);

    Boolean addUserToBlacklist(User user);

    void deleteUserFromBlacklist(Long userId);

    Boolean isBlacklistUser(Long userId);

}
