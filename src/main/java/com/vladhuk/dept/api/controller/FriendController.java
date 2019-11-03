package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.payload.ApiResponse;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/friends")
public class FriendController {

    private UserService userService;

    public FriendController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getFriends(@RequestParam(value = "page", required = false, defaultValue = "0")
                                         Integer pageNumber,
                                 @RequestParam(value = "size", required = false, defaultValue = "9999")
                                         Integer pageSize) {
        if (pageNumber == null) {
            pageNumber = 0;
            pageSize = Integer.MAX_VALUE;
        }

        return userService.getFriends(pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFriend(@PathVariable("id") Long friendId) {
        if (userService.deleteFriend(friendId)) {
            return ResponseEntity.ok(new ApiResponse(false, "You have a debt with friend."));
        }
        return ResponseEntity.ok().build();
    }

}
