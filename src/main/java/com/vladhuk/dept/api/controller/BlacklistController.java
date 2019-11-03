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
    public List<User> getBlacklist(@RequestParam(value = "page", required = false, defaultValue = "0")
                                           Integer pageNumber,
                                   @RequestParam(value = "size", required = false, defaultValue = "9999")
                                           Integer pageSize) {
        return userService.getBlacklist(pageNumber, pageSize);
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
