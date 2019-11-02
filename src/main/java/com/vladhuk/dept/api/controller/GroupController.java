package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.entity.Group;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public class GroupController {

    @GetMapping("/groups")
    public List<Group> getGroups(@RequestParam(value = "page", required = false) Integer pageNumber,
                                 @RequestParam(value = "size", required = false) Integer pageSize) {
        return null;
    }

    @PostMapping
    public Group createGroup(@RequestBody Group group) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteGroup(@PathVariable("id") Long groupId) {
    }

    @PutMapping
    public Group updateGroup(@RequestBody Group group) {
        return null;
    }

}
