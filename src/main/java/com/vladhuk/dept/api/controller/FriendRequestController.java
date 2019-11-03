package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/friend-requests")
public class FriendRequestController {

    private UserService userService;

    public FriendRequestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/sent")
    public List<FriendRequest> getSentFriendRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                             Integer pageNumber,
                                                     @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                             Integer pageSize) {
        return userService.getSentFriendRequests(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<FriendRequest> getReceivedFriendRequests(@RequestParam(value = "page", required = false, defaultValue = "0")
                                                                 Integer pageNumber,
                                                         @RequestParam(value = "size", required = false, defaultValue = "9999")
                                                                 Integer pageSize) {
        return userService.getReceivedFriendRequests(pageNumber, pageSize);
    }

    @PostMapping
    public FriendRequest sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        return userService.sendFriendRequest(friendRequest);
    }

    @PostMapping("/{requestId}/accept")
    public FriendRequest acceptFriendRequest(@PathVariable Long requestId) {
        return userService.acceptFriendRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public FriendRequest rejectFriendRequest(@PathVariable Long requestId) {
        return userService.rejectFriendRequest(requestId);
    }

}
