package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.Group;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.repository.GroupRepository;
import com.vladhuk.dept.api.service.AuthenticationService;
import com.vladhuk.dept.api.service.GroupService;
import com.vladhuk.dept.api.service.UserService;
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
        return null;
    }

    @Override
    public List<Group> getGroupsPage(Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Group getGroup(Long groupId) {
        return null;
    }

    @Override
    public Group createGroup(Group group) {
        return null;
    }

    @Override
    public void deleteGroup(Long groupId) {
        groupRepository.deleteById(groupId);
    }

    @Override
    public Group updateGroup(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group addMember(Long groupId, User member) {
        final Optional<Group> optionalGroup = groupRepository.findById(groupId);
        final User foundedUser = userService.getUser(member.getId());

        if (optionalGroup.isPresent() && isCurrentUserOwner(optionalGroup.get())) {
            final Group group = optionalGroup.get();
            group.getMembers().add(foundedUser);

            return groupRepository.save(group);
        }

        return null;
    }

    @Override
    public Group deleteMember(Long groupId, Long memberId) {
        return null;
    }

    @Override
    public Boolean isCurrentUserOwner(Group group) {
        return Objects.equals(group.getOwner(), authenticationService.getCurrentUser());
    }

}
