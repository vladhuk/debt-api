package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.User;

public interface UserService {

    User getUser(Long userId);

    User getUser(String username);

    User addUser(User user);

    Boolean isUserExists(String username);

}
