package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.BadRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.UserRepository;
import com.vladhuk.debt.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(Long userId) {
        final Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            logger.error("User with id {} not founded", userId);
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return user.get();
    }

    @Override
    public User getUser(String username) {
        final Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            logger.error("User with username {} not founded", username);
            throw new ResourceNotFoundException("User", "id", username);
        }

        return user.get();
    }

    @Override
    public User getUser(User user) {
        if (user.getId() != null) {
            return getUser(user.getId());
        } else if (user.getUsername() != null) {
            return getUser(user.getUsername());
        }
        logger.error("Can not fetch user without id and username");
        throw new BadRequestException("You should to specify user id or username");
    }

    @Override
    public User addUser(User user) {
        logger.info("Saving new user with username {}", user.getUsername());
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) {
        logger.info("Updating user with id {}", user.getId());
        return userRepository.save(user);
    }

    @Override
    public Boolean isUserExists(String username) {
        return userRepository.existsByUsername(username);
    }

}
