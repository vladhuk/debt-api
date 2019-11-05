package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Group;

import java.util.List;

public interface GroupService {

    List<Group> getAllGroups();

    List<Group> getGroupsPage(Integer pageNumber, Integer pageSize);

    Group getGroup(Long groupId);

    Group createGroup(Group group);

    void deleteGroup(Long groupId);

    Group updateGroup(Group group);

    Group addMember(Long groupId, Long memberId);

    Group deleteMember(Long groupId, Long memberId);

    Boolean isCurrentUserOwner(Group group);

}
