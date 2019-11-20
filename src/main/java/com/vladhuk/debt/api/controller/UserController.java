package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.BlacklistService;
import com.vladhuk.debt.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller("/users")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final BlacklistService blacklistService;

    public UserController(UserService userService, AuthenticationService authenticationService, BlacklistService blacklistService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.blacklistService = blacklistService;
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        final User user = userService.getUser(username);

        if (blacklistService.isUsersBlacklistContainsCurrentUser(user.getId())) {
            logger.error("Can not fetch user with username {}, because he has user with id {} in blacklist", username, user.getId());
            throw new ResourceNotFoundException("User", "username", username);
        }

        return user;
    }

}
