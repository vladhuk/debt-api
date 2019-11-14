package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.BlacklistRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.BlacklistService;
import com.vladhuk.debt.api.service.FriendService;
import com.vladhuk.debt.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlacklistServiceImpl implements BlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(BlacklistServiceImpl.class);

    private final BlacklistRepository blacklistRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final FriendService friendService;

    public BlacklistServiceImpl(BlacklistRepository blacklistRepository, AuthenticationService authenticationService, UserService userService, FriendService friendService) {
        this.blacklistRepository = blacklistRepository;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.friendService = friendService;
    }

    @Override
    public List<User> getFullBlacklist() {
        final User currentUser = authenticationService.getCurrentUser();
        logger.info("Fetching blacklist of user with id {}", currentUser.getId());
        return currentUser.getBlacklist();
    }

    @Override
    public List<User> getBlacklistPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());

        logger.info("Fetching blacklist page of user with id {}", currentUserId);

        return blacklistRepository.findAllBlackUsersByUserId(currentUserId, pageable);
    }

    @Override
    public void addUserToBlacklist(User user) {
        final User currentUser = authenticationService.getCurrentUser();
        final User userForBlackList = userService.getUser(user);

        logger.info("Adding user {} to blacklist of user {}", userForBlackList.getId(), user.getId());

        if (friendService.isFriend(userForBlackList.getId())) {
            friendService.deleteFriendship(userForBlackList.getId());
        }

        currentUser.getBlacklist().add(userForBlackList);
        userService.updateUser(currentUser);
    }

    @Override
    public void deleteUserFromBlacklist(Long userId) {
        final User currentUser = authenticationService.getCurrentUser();
        final User userForDelete = userService.getUser(userId);

        logger.info("Deleting user {} from blacklist of user {}", userForDelete.getId(), currentUser.getId());

        currentUser.getBlacklist().remove(userForDelete);
    }

    @Override
    public Boolean isUsersBlacklistContainsCurrentUser(Long userId) {
        final User user = userService.getUser(userId);
        return user.getBlacklist().contains(authenticationService.getCurrentUser());
    }
}
