package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.UserRepository;
import com.vladhuk.debt.api.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

}