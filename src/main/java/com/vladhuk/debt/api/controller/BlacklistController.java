package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.BlacklistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/blacklist")
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
    public void addUserToBlacklist(@RequestBody User user) {
        blacklistService.addUserToBlacklist(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUserFromBlacklist(@PathVariable("id") Long userId) {
        blacklistService.deleteUserFromBlacklist(userId);
    }

}
