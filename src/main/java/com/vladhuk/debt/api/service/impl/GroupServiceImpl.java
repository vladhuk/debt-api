package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.exception.UserNotFriendException;
import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.GroupRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.FriendService;
import com.vladhuk.debt.api.service.GroupService;
import com.vladhuk.debt.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
public class GroupServiceImpl implements GroupService {

    private static final Logger logger = LoggerFactory.getLogger(GroupServiceImpl.class);

    private final GroupRepository groupRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final FriendService friendService;

    public GroupServiceImpl(GroupRepository groupRepository, AuthenticationService authenticationService, UserService userService, FriendService friendService) {
        this.groupRepository = groupRepository;
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.friendService = friendService;
    }

    @Override
    public List<Group> getAllGroups() {
        final Long ownerId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching groups of user with id {}", ownerId);
        return groupRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Group> getGroupsPage(Integer pageNumber, Integer pageSize) {
        final Long ownerId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching groups page of user with id {}", ownerId);
        return groupRepository.findByOwnerId(ownerId, PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending()));
    }

    @Override
    public Group getGroup(Long groupId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Optional<Group> group = groupRepository.findByIdAndOwnerId(groupId, currentUserId);

        if (group.isEmpty()) {
            logger.error("User with id {} does not have a group with id {}", currentUserId, groupId);
            throw new ResourceNotFoundException("User", "group id", groupId);
        }

        return group.get();
    }

    @Override
    public Group createGroup(Group group) {
        group.setOwner(authenticationService.getCurrentUser());
        logger.info("Creating group: {}", group);
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long groupId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Deleting group with id {} with owner {}", groupId, currentUserId);
        groupRepository.deleteByIdAndOwnerId(groupId, currentUserId);
    }

    @Override
    public Group updateGroup(Group group) {
        final User currentUser = authenticationService.getCurrentUser();

        logger.info("Updating group with id {} with owner {}", group.getId(), currentUser.getId());

        if (!Objects.equals(group.getOwner(), currentUser)) {
            logger.error("Current user with id {} does not have a group with id {}", currentUser.getId(), group.getId());
            throw new ResourceNotFoundException("User", "group id", group.getId());
        }
        return groupRepository.save(group);
    }

    @Override
    public Group addMember(Long groupId, User member) {
        final Group group = getGroup(groupId);
        final User newMember = userService.getUser(member);

        logger.info("Adding member with id {} to group with id {}", newMember.getId(), groupId);

        if (!friendService.isFriend(newMember.getId())) {
            logger.info("User with id {} can not add member with id {} to group with id {}, because he is not friend", group.getOwner().getId(), group.getId(), newMember.getId());
            throw new UserNotFriendException("Can not add to group. Users " + group.getOwner().getId() + " and " + newMember.getId() + " are not friends");
        }

        group.getMembers().add(newMember);

        return groupRepository.save(group);
    }

    @Override
    public Group deleteMember(Long groupId, Long memberId) {
        final Group group = getGroup(groupId);
        final User member = userService.getUser(memberId);

        logger.info("Deleting member with id {} to group with id {}", memberId, groupId);
        group.getMembers().remove(member);

        return groupRepository.save(group);
    }

}
