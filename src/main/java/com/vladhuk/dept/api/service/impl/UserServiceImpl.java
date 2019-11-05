package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.repository.UserRepository;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public User getUser(String username) {
        return userRepository.findByUsername(username).get();
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public Boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

}
