package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.exception.AppException;
import com.vladhuk.dept.api.model.Role;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.repository.RoleRepository;
import com.vladhuk.dept.api.security.JwtTokenProvider;
import com.vladhuk.dept.api.service.AuthenticationService;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public String authenticateAndGetToken(String username, String password) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenProvider.generateToken(authentication);
    }

    @Override
    public User registerUser(User user) {

        final User newUser = new User();
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));

        final Role userRole = roleRepository.findByName(Role.RoleName.USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        newUser.setRoles(Collections.singleton(userRole));

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
