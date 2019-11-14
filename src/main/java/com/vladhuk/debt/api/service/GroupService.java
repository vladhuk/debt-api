package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;

import java.util.List;

public interface GroupService {

    List<Group> getAllGroups();

    List<Group> getGroupsPage(Integer pageNumber, Integer pageSize);

    Group getGroup(Long groupId);

    Group createGroup(Group group);

    void deleteGroup(Long groupId);

    Group updateGroup(Group group);

    Group addMember(Long groupId, User member);

    Group deleteMember(Long groupId, Long memberId);

}
