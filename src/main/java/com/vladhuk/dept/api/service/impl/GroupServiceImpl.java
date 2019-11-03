package com.vladhuk.dept.api.service.impl;

import com.vladhuk.dept.api.model.Group;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Override
    public List<Group> getGroups(Integer pageNumber, Integer pageSize) {
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

    }
    @Override
    public Group updateGroup(Group group) {
        return null;
    }
    @Override
    public Group addMember(Long groupId, User member) {
        return null;
    }
    @Override
    public Group deleteMember(Long groupId, Long memberId) {
        return null;
    }
}
