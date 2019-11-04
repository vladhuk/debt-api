package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/blacklist")
public class BlacklistController {

    private UserService userService;

    public BlacklistController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getFullBlackList() {
        return userService.getFullBlacklist();
    }

    @GetMapping
    public List<User> getBlacklistPage(@RequestParam(value = "page") Integer pageNumber,
                                       @RequestParam(value = "size") Integer pageSize) {
        return userService.getBlacklistPage(pageNumber, pageSize);
    }

    @PostMapping
    public List<User> addUserToBlacklist(@RequestBody User user) {
        return userService.addUserToBlacklist(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserFromBlacklist(@PathVariable("id") Long userId) {
        userService.deleteUserFromBlacklist(userId);
    }

}
