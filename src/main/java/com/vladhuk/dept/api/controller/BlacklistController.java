package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/blacklist")
public class BlacklistController {

    @GetMapping
    public List<User> getBlacklist(@RequestParam(value = "page", required = false) Integer pageNumber,
                                   @RequestParam(value = "size", required = false) Integer pageSize) {
        return null;
    }

    @PostMapping
    public User addUserToBlacklist(@RequestBody User friend) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteUserFromBlacklist(@PathVariable("id") Long friendId) {
    }

}
