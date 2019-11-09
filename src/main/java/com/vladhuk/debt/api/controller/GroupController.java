package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> updateGroup(@RequestBody Group group) {
        final Optional<Group> updatedGroup = groupService.updateGroup(group);
        return updatedGroup.isPresent()
                ? ResponseEntity.ok(updatedGroup.get())
                : ResponseEntity.badRequest().build();
    }

    @PostMapping("/groups/{id}/members")
    public ResponseEntity<?> addMember(@PathVariable Long id, @RequestBody User member) {
        final Optional<Group> group = groupService.addMember(id, member);
        return group.isPresent()
                ? ResponseEntity.ok(group.get())
                : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/groups/{groupId}/members/{memberId}")
    public ResponseEntity<?> deleteMember(@PathVariable Long groupId, @PathVariable Long memberId) {
        final Optional<Group> group = groupService.deleteMember(groupId, memberId);
        return group.isPresent()
                ? ResponseEntity.ok(group.get())
                : ResponseEntity.badRequest().build();
    }

}
