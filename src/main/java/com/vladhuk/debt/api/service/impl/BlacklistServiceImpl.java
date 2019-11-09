package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.BlacklistRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.BlacklistService;
import com.vladhuk.debt.api.service.FriendService;
import com.vladhuk.debt.api.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BlacklistServiceImpl implements BlacklistService {

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
        return authenticationService.getCurrentUser().getBlacklist();
    }

    @Override
    public List<User> getBlacklistPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        return blacklistRepository.findAllBlackUsersByUserId(currentUserId, pageable);
    }

    @Override
    public Boolean addUserToBlacklist(User user) {
        final User currentUser = authenticationService.getCurrentUser();
        final User userForBlackList = userService.getUser(user);

        if (!friendService.isFriend(userForBlackList.getId())
                || (friendService.isFriend(userForBlackList.getId()) && friendService.deleteFriendship(userForBlackList.getId()))) {

            currentUser.getBlacklist().add(userForBlackList);
            userService.updateUser(currentUser);
            return true;
        }

        return false;
    }

    @Override
    public void deleteUserFromBlacklist(Long userId) {
        final User currentUser = authenticationService.getCurrentUser();
        final User userForDelete = userService.getUser(userId);

        currentUser.getBlacklist().remove(userForDelete);
    }

    @Override
    public Boolean isBlacklistUser(Long userId) {
        final User user = userService.getUser(userId);
        return authenticationService.getCurrentUser().getBlacklist().contains(user);
    }

}
