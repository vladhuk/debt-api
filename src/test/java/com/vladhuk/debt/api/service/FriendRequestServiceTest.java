package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.FriendRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.FriendRequest;
import com.vladhuk.debt.api.model.Status;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.FriendRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.vladhuk.debt.api.model.Status.StatusName.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FriendRequestServiceTest {

    @Autowired
    private FriendRequestService friendRequestService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private FriendRequestRepository friendRequestRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private FriendService friendService;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;
    private FriendRequest sentRequest;
    private FriendRequest receivedRequest;
    private Status sentStatus;
    private Status viewedStatus;
    private Status confirmedStatus;
    private Status rejectedStatus;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        sentStatus = statusService.getStatus(SENT);
        viewedStatus = statusService.getStatus(VIEWED);
        confirmedStatus = statusService.getStatus(CONFIRMED);
        rejectedStatus = statusService.getStatus(REJECTED);

        sentRequest = new FriendRequest();
        sentRequest.setSender(registeredTestUser1);
        sentRequest.setReceiver(registeredTestUser2);
        sentRequest.setStatus(sentStatus);
        sentRequest = friendRequestRepository.save(sentRequest);

        receivedRequest = new FriendRequest();
        receivedRequest.setSender(registeredTestUser2);
        receivedRequest.setReceiver(registeredTestUser1);
        receivedRequest.setStatus(sentStatus);
        receivedRequest = friendRequestRepository.save(receivedRequest);
    }

    @Test
    public void getAllSentFriendRequests_When_UserSentRequests_Expected_SentRequests() {
        final List<FriendRequest> friendRequests = friendRequestService.getAllSentFriendRequests();

        assertEquals(1, friendRequests.size());
        assertEquals(sentRequest, friendRequests.get(0));
    }

    @Test
    public void changeStatusToViewed_When_StatusSent_Expected_Viewed() {
        final FriendRequest viewedRequest =
                friendRequestService.changeStatusToViewed(Collections.singletonList(sentRequest)).get(0);
        assertEquals(VIEWED, viewedRequest.getStatus().getName());

        final Optional<FriendRequest> updatedRequest = friendRequestRepository.findById(sentRequest.getId());
        assertTrue(updatedRequest.isPresent());
        assertEquals(VIEWED, updatedRequest.get().getStatus().getName());
    }

    @Test
    public void getAllReceivedFriendRequests_When_UserReceivedRequests_Expected_ReceivedAndViewedRequests() {
        FriendRequest receivedConfirmedRequest = new FriendRequest();
        receivedConfirmedRequest.setSender(registeredTestUser2);
        receivedConfirmedRequest.setReceiver(registeredTestUser1);
        receivedConfirmedRequest.setStatus(confirmedStatus);
        receivedConfirmedRequest = friendRequestRepository.save(receivedConfirmedRequest);

        final List<FriendRequest> friendRequests = friendRequestService.getAllReceivedFriendRequests();

        receivedRequest.setStatus(viewedStatus);

        assertEquals(2, friendRequests.size());
        assertEquals(receivedRequest, friendRequests.get(0));
        assertEquals(receivedConfirmedRequest, friendRequests.get(1));
    }

    @Test
    public void countNewReceivedFriendRequests_WhenReceived() {
        FriendRequest receivedConfirmedRequest = new FriendRequest();
        receivedConfirmedRequest.setSender(registeredTestUser2);
        receivedConfirmedRequest.setReceiver(registeredTestUser1);
        receivedConfirmedRequest.setStatus(confirmedStatus);
        friendRequestRepository.save(receivedConfirmedRequest);

        assertEquals(1, friendRequestService.countNewReceivedFriendRequests());
    }

    @Test
    public void sendFriendRequest_When_UserNotInBlackList_Expected_Request() {
        final FriendRequest requestForSending = new FriendRequest();
        requestForSending.setSender(new User("will not save", "will not save", "will not save"));
        requestForSending.setReceiver(registeredTestUser2);
        requestForSending.setComment("Comment");
        requestForSending.setStatus(rejectedStatus);

        final FriendRequest sentRequest = friendRequestService.sendFriendRequest(requestForSending);
        assertEquals(authenticationService.getCurrentUser(), sentRequest.getSender());
        assertEquals(requestForSending.getReceiver(), sentRequest.getReceiver());
        assertEquals(requestForSending.getComment(), sentRequest.getComment());
        assertEquals(SENT, sentRequest.getStatus().getName());
    }

    @Test
    public void sendFriendRequest_When_UserInBlackList_Expected_FriendRequestException() {
        blacklistService.addUserToBlacklist(registeredTestUser2);

        authenticationService.authenticateAndGetToken(testUser2.getUsername(), testUser2.getPassword());

        final FriendRequest requestForSending = new FriendRequest();
        requestForSending.setSender(registeredTestUser2);
        requestForSending.setReceiver(registeredTestUser1);

        assertThrows(FriendRequestException.class, () -> friendRequestService.sendFriendRequest(requestForSending));
    }

    @Test
    public void sendFriendRequest_When_UserIsFriend_Expected_FriendRequestException() {
        friendService.createFriendship(registeredTestUser2.getId());

        final FriendRequest requestForSending = new FriendRequest();
        requestForSending.setSender(registeredTestUser1);
        requestForSending.setReceiver(registeredTestUser2);

        assertThrows(FriendRequestException.class, () -> friendRequestService.sendFriendRequest(requestForSending));
    }

    @Test
    public void confirmFriendRequest_When_StatusViewed_Expected_Accepted() {
        receivedRequest.setStatus(viewedStatus);
        receivedRequest = friendRequestRepository.save(receivedRequest);

        final FriendRequest confirmedRequest = friendRequestService.confirmFriendRequest(receivedRequest.getId());
        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        receivedRequest.setStatus(confirmedStatus);
        assertEquals(receivedRequest, confirmedRequest);
    }

    @Test
    public void confirmFriendRequest_When_StatusNotViewed_Expected_ResourceNotFoundException() {
        assertThrows(ResourceNotFoundException.class, () -> friendRequestService.confirmFriendRequest(receivedRequest.getId()));
    }

}
