package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.FriendRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.DebtService;
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
public class FriendServiceImpl implements FriendService {

    private final AuthenticationService authenticationService;
    private final DebtService debtService;
    private final UserService userService;
    private final FriendRepository friendRepository;

    public FriendServiceImpl(AuthenticationService authenticationService, DebtService debtService, UserService userService, FriendRepository friendRepository) {
        this.authenticationService = authenticationService;
        this.debtService = debtService;
        this.userService = userService;
        this.friendRepository = friendRepository;
    }

    @Override
    public List<User> getAllFriends() {
        return authenticationService.getCurrentUser().getFriends();
    }

    @Override
    public List<User> getFriendsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("name").ascending());
        return friendRepository.findAllFriendsByUserId(currentUserId, pageable);
    }

    @Override
    public Boolean deleteFriendship(Long friendId) {
        if (debtService.isExistsDebtWithUser(friendId)) {
            return false;
        }

        User currentUser = authenticationService.getCurrentUser();
        User friendForDelete = userService.getUser(friendId);

        currentUser.getFriends().remove(friendForDelete);
        userService.updateUser(currentUser);

        friendForDelete.getFriends().remove(currentUser);
        userService.updateUser(friendForDelete);

        return true;
    }

    @Override
    public User createFriendship(Long friendId) {
        final User currentUser = authenticationService.getCurrentUser();
        final User newFriend = userService.getUser(friendId);

        currentUser.getFriends().add(newFriend);
        friendRepository.save(currentUser);

        newFriend.getFriends().add(currentUser);
        friendRepository.save(newFriend);

        return currentUser;
    }

    @Override
    public Boolean isFriend(Long userId) {
        final User user = userService.getUser(userId);
        return authenticationService.getCurrentUser().getFriends().contains(user);
    }
}
