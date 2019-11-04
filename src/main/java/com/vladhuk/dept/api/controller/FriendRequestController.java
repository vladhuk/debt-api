package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.FriendRequest;
import com.vladhuk.dept.api.service.FriendRequestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-management/friend-requests")
public class FriendRequestController {

    private FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/sent")
    public List<FriendRequest> getAllSentFriendRequests() {
        return friendRequestService.getAllSentFriendRequests();
    }

    @GetMapping(value = "/sent", params = {"page", "size"})
    public List<FriendRequest> getSentFriendRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                         @RequestParam(value = "size") Integer pageSize) {
        return friendRequestService.getSentFriendRequestsPage(pageNumber, pageSize);
    }

    @GetMapping("/received")
    public List<FriendRequest> getAllReceivedFriendRequests() {
        return friendRequestService.getAllReceivedFriendRequests();
    }

    @GetMapping(value = "/received", params = {"page", "size"})
    public List<FriendRequest> getReceivedFriendRequestsPage(@RequestParam(value = "page") Integer pageNumber,
                                                             @RequestParam(value = "size") Integer pageSize) {
        return friendRequestService.getReceivedFriendRequestsPage(pageNumber, pageSize);
    }

    @PostMapping
    public FriendRequest sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        return friendRequestService.sendFriendRequest(friendRequest);
    }

    @PostMapping("/{requestId}/accept")
    public FriendRequest acceptFriendRequest(@PathVariable Long requestId) {
        return friendRequestService.acceptFriendRequest(requestId);
    }

    @PostMapping("/{requestId}/reject")
    public FriendRequest rejectFriendRequest(@PathVariable Long requestId) {
        return friendRequestService.rejectFriendRequest(requestId);
    }

}