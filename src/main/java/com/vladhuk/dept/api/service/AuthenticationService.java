package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.User;

public interface AuthenticationService {

    String authenticateAndGetToken(String username, String password);

    User registerUser(User user);

    Boolean isUsernameExist(String username);

    void logout();

}
