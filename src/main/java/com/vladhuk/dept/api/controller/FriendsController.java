package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management//friends")
public class FriendsController {

    @GetMapping
    public List<User> getFriends(@RequestParam(value = "page", required = false) Integer pageNumber,
                                 @RequestParam(value = "size", required = false) Integer pageSize) {
        return null;
    }

    @PostMapping
    public User addFriend(@RequestBody User friend) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteFriend(@PathVariable("id") Long friendId) {
    }

}
