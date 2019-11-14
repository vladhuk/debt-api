package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.BadRequestException;
import com.vladhuk.debt.api.model.Role;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.security.JwtTokenProvider;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.RoleService;
import com.vladhuk.debt.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserService userService, RoleService roleService, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String authenticateAndGetToken(String username, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        logger.info("Authenticating user with username {}", username);

        return tokenProvider.generateToken(authentication);
    }

    @Override
    public User registerUser(User user) {
        if (isUsernameExist(user.getUsername())) {
            logger.error("User with username {} already exist", user.getUsername());
            throw new BadRequestException("User with username " + user.getUsername() + " already exist");
        }

        final User newUser = new User();
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        final Role userRole = roleService.getRole(Role.RoleName.USER);

        newUser.getRoles().add(userRole);

        logger.info("Registering new user with username {}", newUser.getUsername());

        return userService.addUser(newUser);
    }

    @Override
    public Boolean isUsernameExist(String username) {
        return userService.isUserExists(username);
    }

    @Override
    public User getCurrentUser() {
        final String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUser(currentUsername);
    }

}
