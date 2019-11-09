package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.GroupRepository;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.GroupService;
import com.vladhuk.debt.api.service.UserService;
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

    private final GroupRepository groupRepository;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    public GroupServiceImpl(GroupRepository groupRepository, AuthenticationService authenticationService, UserService userService) {
        this.groupRepository = groupRepository;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public List<Group> getAllGroups() {
        final Long ownerId = authenticationService.getCurrentUser().getId();
        return groupRepository.findByOwnerId(ownerId);
    }

    @Override
    public List<Group> getGroupsPage(Integer pageNumber, Integer pageSize) {
        final Long ownerId = authenticationService.getCurrentUser().getId();
        return groupRepository.findByOwnerId(ownerId, PageRequest.of(pageNumber, pageSize, Sort.by("title").ascending()));
    }

    @Override
    public Group getGroup(Long groupId) {
        final Long ownerId = authenticationService.getCurrentUser().getId();
        return groupRepository.findByIdAndOwnerId(groupId, ownerId).orElse(null);
    }

    @Override
    public Group createGroup(Group group) {
        group.setOwner(authenticationService.getCurrentUser());
        return groupRepository.save(group);
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteByIdAndOwnerId(groupId, authenticationService.getCurrentUser().getId());
    }

    @Override
    public Optional<Group> updateGroup(Group group) {
        if (Objects.equals(group.getOwner(), authenticationService.getCurrentUser())) {
            return Optional.of(groupRepository.save(group));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Group> addMember(Long groupId, User member) {
        final User currentUser = authenticationService.getCurrentUser();
        final User newMember = userService.getUser(member);
        final Optional<Group> optionalGroup = groupRepository.findByIdAndOwnerId(groupId, currentUser.getId());

        if (optionalGroup.isPresent()) {
            final Group group = optionalGroup.get();
            group.getMembers().add(newMember);

            return Optional.of(groupRepository.save(group));
        }
        return Optional.empty();
    }

    @Override
    public Optional<Group> deleteMember(Long groupId, Long memberId) {
        final User currentUser = authenticationService.getCurrentUser();
        final User member = userService.getUser(memberId);
        final Optional<Group> optionalGroup = groupRepository.findByIdAndOwnerId(groupId, currentUser.getId());

        if (optionalGroup.isPresent()) {
            final Group group = optionalGroup.get();
            group.getMembers().remove(member);

            return Optional.of(groupRepository.save(group));
        }

        return Optional.empty();
    }

}
