package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.FriendRequest;
import com.vladhuk.debt.api.service.FriendRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/received/new/count")
    public Long countNewReceivedFriendRequests() {
        return friendRequestService.countNewReceivedFriendRequests();
    }

    @PostMapping
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequest friendRequest) {
        final Optional<FriendRequest> requestForSend = friendRequestService.sendFriendRequest(friendRequest);

        return requestForSend.isPresent()
                ? ResponseEntity.ok(requestForSend.get())
                : ResponseEntity.badRequest().build();
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
