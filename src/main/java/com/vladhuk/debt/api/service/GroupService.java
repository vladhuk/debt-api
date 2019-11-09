package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;

import java.util.List;
import java.util.Optional;

public interface GroupService {

    List<Group> getAllGroups();

    List<Group> getGroupsPage(Integer pageNumber, Integer pageSize);

    Group getGroup(Long groupId);

    Group createGroup(Group group);

    void deleteGroup(Long groupId);

    Optional<Group> updateGroup(Group group);

    Optional<Group> addMember(Long groupId, User member);

    Optional<Group> deleteMember(Long groupId, Long memberId);

}
