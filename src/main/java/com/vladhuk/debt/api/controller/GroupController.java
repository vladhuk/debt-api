package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.GroupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/groups")
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

    @PostMapping("/{id}/members")
    public Group addMember(@PathVariable Long id, @RequestBody User member) {
        return groupService.addMember(id, member);
    }

    @DeleteMapping("/{groupId}/members/{memberId}")
    public Group deleteMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        return groupService.deleteMember(groupId, memberId);
    }

}
