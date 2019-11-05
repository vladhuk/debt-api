package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.Group;
import com.vladhuk.dept.api.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/groups")
public class GroupController {

    private GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public List<Group> getAllGroups() {
        return groupService.getAllGroups();
    }

    @GetMapping(params = {"page", "size"})
    public List<Group> getGroupsPage(@RequestParam(value = "page") Integer pageNumber,
                                     @RequestParam(value = "size") Integer pageSize) {
        return groupService.getGroupsPage(pageNumber, pageSize);
    }

    @GetMapping("/{id}")
    public Group getGroup(@PathVariable Long id) {
        return groupService.getGroup(id);
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return groupService.createGroup(group);
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @PutMapping
    public Group updateGroup(@RequestBody Group group) {
        return groupService.updateGroup(group);
    }

    @PutMapping("/groups/{id}/members/{memberId}")
    public Group addMember(@PathVariable Long id, @PathVariable Long memberId) {
        return groupService.addMember(id, memberId);
    }

    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public Group deleteMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        return groupService.deleteMember(groupId, memberId);
    }

}
