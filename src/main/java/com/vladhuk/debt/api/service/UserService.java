package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.User;

public interface UserService {

    User getUser(Long userId);

    User getUser(String username);

    User addUser(User user);

    Boolean isUserExists(String username);

}
