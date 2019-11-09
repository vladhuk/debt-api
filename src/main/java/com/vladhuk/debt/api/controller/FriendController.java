package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/friends")
public class FriendController {

    private FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping
    public List<User> getAllFriends() {
        return friendService.getAllFriends();
    }

    @GetMapping(params = {"page", "size"})
    public List<User> getFriendsPage(@RequestParam(value = "page") Integer pageNumber,
                                     @RequestParam(value = "size") Integer pageSize) {
        return friendService.getFriendsPage(pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFriend(@PathVariable("id") Long friendId) {
        return friendService.deleteFriendship(friendId) == null
                ? ResponseEntity.badRequest().build()
                : ResponseEntity.ok().build();
    }

}
