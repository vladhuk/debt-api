package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.User;

public interface AuthenticationService {

    String authenticateAndGetToken(String username, String password);

    User registerUser(User user);

    Boolean isUsernameExist(String username);

    User getCurrentUser();

}
