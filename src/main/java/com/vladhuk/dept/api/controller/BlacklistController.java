package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/blacklist")
public class BlacklistController {

    private BlacklistService blacklistService;

    public BlacklistController(BlacklistService blacklistService) {
        this.blacklistService = blacklistService;
    }

    @GetMapping
    public List<User> getFullBlackList() {
        return blacklistService.getFullBlacklist();
    }

    @GetMapping(params = {"page", "size"})
    public List<User> getBlacklistPage(@RequestParam(value = "page") Integer pageNumber,
                                       @RequestParam(value = "size") Integer pageSize) {
        return blacklistService.getBlacklistPage(pageNumber, pageSize);
    }

    @PostMapping
    public List<User> addUserToBlacklist(@RequestBody User user) {
        return blacklistService.addUserToBlacklist(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserFromBlacklist(@PathVariable("id") Long userId) {
        blacklistService.deleteUserFromBlacklist(userId);
    }

}
