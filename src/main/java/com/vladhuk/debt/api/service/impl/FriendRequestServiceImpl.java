package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.FriendRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.FriendRequest;
import com.vladhuk.debt.api.model.Status;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.FriendRequestRepository;
import com.vladhuk.debt.api.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.vladhuk.debt.api.model.Status.StatusName.*;

@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    private static final Logger logger = LoggerFactory.getLogger(FriendRequestServiceImpl.class);

    private final FriendRequestRepository friendRequestRepository;
    private final AuthenticationService authenticationService;
    private final StatusService statusService;
    private final UserService userService;
    private final FriendService friendService;
    private final BlacklistService blacklistService;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, AuthenticationService authenticationService, StatusService statusService, UserService userService, FriendService friendService, BlacklistService blacklistService) {
        this.friendRequestRepository = friendRequestRepository;
        this.authenticationService = authenticationService;
        this.statusService = statusService;
        this.userService = userService;
        this.friendService = friendService;
        this.blacklistService = blacklistService;
    }

    @Override
    public List<FriendRequest> getAllSentFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all sent from user with id {} friend requests", currentUserId);
        return friendRequestRepository.findAllBySenderId(currentUserId);
    }

    @Override
    public List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching sent from user with id {} friend requests page", currentUserId);

        return friendRequestRepository.findAllBySenderId(currentUserId, pageable);
    }

    @Override
    public List<FriendRequest> changeStatusToViewed(List<FriendRequest> requests) {
        final Status viewedStatus = statusService.getStatus(VIEWED);

        requests.stream()
                .filter(request -> request.getStatus().getName() == SENT)
                .forEach(request -> {
                    request.setStatus(viewedStatus);
                    friendRequestRepository.save(request);
                });

        return requests;
    }

    @Override
    public List<FriendRequest> getAllReceivedFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        logger.info("Fetching all received by user with id {} friend requests", currentUserId);
        return changeStatusToViewed(friendRequestRepository.findAllByReceiverId(currentUserId));
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());

        logger.info("Fetching received by user with id {} friend requests page", currentUserId);

        return changeStatusToViewed(friendRequestRepository.findAllByReceiverId(currentUserId, pageable));
    }

    @Override
    public Long countNewReceivedFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long statusId = statusService.getStatus(SENT).getId();

        logger.info("Counting new received by user with id {} friend requests", currentUserId);

        return friendRequestRepository.countAllByReceiverIdAndStatusId(currentUserId, statusId);
    }

    @Override
    public void deleteSentFriendRequestIfNotConfirmedOrRejected(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Optional<FriendRequest> friendRequest = friendRequestRepository.findByIdAndSenderId(requestId, currentUserId);

        logger.info("Deleting friend request with id {}", requestId);

        if (friendRequest.isEmpty() || friendRequest.get().getStatus().getName() == CONFIRMED
                || friendRequest.get().getStatus().getName() == REJECTED) {
            logger.error("Not rejected or confirmed friend request with id {} and sender id {} not founded", requestId, currentUserId);
            throw new ResourceNotFoundException("Not rejected or confirmed friend request", "id", requestId);
        }
        friendRequestRepository.deleteById(requestId);
    }

    @Override
    public FriendRequest sendFriendRequest(FriendRequest friendRequest) {
        final User currentUser = authenticationService.getCurrentUser();
        final User receiver = userService.getUser(friendRequest.getReceiver());

        logger.info("Sending friend request from user {} to user {}", currentUser.getId(), receiver.getId());

        if (friendService.isFriend(receiver.getId())) {
            logger.error("User {} can not send friend request to user with id {}, because they are friends", currentUser.getId(), receiver.getId());
            throw new FriendRequestException("Can not send request because users are friends");
        }

        if (blacklistService.isUsersBlacklistContainsCurrentUser(receiver.getId())) {
            logger.error("User {} can not send friend request to user with id {}, because receiver have user in blacklist", currentUser.getId(), receiver.getId());
            throw new FriendRequestException("Can not send request because receiver have user in blacklist");
        }

        final FriendRequest requestForSave = new FriendRequest();
        requestForSave.setSender(currentUser);
        requestForSave.setReceiver(receiver);
        requestForSave.setComment(friendRequest.getComment());
        requestForSave.setStatus(statusService.getStatus(SENT));

        return friendRequestRepository.save(requestForSave);
    }

    private FriendRequest getViewedReceivedFriendRequest(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long viewedStatusId = statusService.getStatus(VIEWED).getId();
        final Optional<FriendRequest> optionalFriendRequest =
                friendRequestRepository.findByIdAndReceiverIdAndStatusId(requestId, currentUserId, viewedStatusId);

        if (optionalFriendRequest.isEmpty()) {
            logger.error("Friend request with id {} and status VIEWED for user with id {} not founded", requestId, currentUserId);
            throw new ResourceNotFoundException("Friend request with status VIEWD", "id", requestId);
        }

        return optionalFriendRequest.get();
    }

    @Override
    public FriendRequest confirmFriendRequestAndDeleteSameViewed(Long requestId) {
        logger.info("Confirming friend request with id {}", requestId);

        final FriendRequest friendRequest = getViewedReceivedFriendRequest(requestId);
        friendRequest.setStatus(statusService.getStatus(CONFIRMED));

        final FriendRequest savedFriendRequest = friendRequestRepository.save(friendRequest);
        final Long senderId = savedFriendRequest.getSender().getId();
        final Long receiverId = savedFriendRequest.getReceiver().getId();

        deleteNotConfirmedAndNotRejectedFriendRequests(senderId, receiverId);

        if (friendService.isFriend(senderId)) {
            friendRequestRepository.deleteById(savedFriendRequest.getId());
            logger.error("Can not confirm friend request {}. User {} already have friendship with user {}", savedFriendRequest.getId(), senderId, receiverId);
            throw new FriendRequestException("Users " + senderId + " and " + receiverId + " already friends");
        }

        friendService.createFriendship(senderId);

        return savedFriendRequest;
    }

    private void deleteNotConfirmedAndNotRejectedFriendRequests(Long senderId, Long receiverId) {
        friendRequestRepository.deleteAllBySenderIdAndReceiverIdAndStatusId(
                senderId, receiverId, statusService.getStatus(VIEWED).getId()
        );
        friendRequestRepository.deleteAllBySenderIdAndReceiverIdAndStatusId(
                senderId, receiverId, statusService.getStatus(SENT).getId()
        );
        friendRequestRepository.deleteAllBySenderIdAndReceiverIdAndStatusId(
                receiverId, senderId, statusService.getStatus(VIEWED).getId()
        );
        friendRequestRepository.deleteAllBySenderIdAndReceiverIdAndStatusId(
                receiverId, senderId, statusService.getStatus(SENT).getId()
        );
    }

    @Override
    public FriendRequest rejectFriendRequest(Long requestId) {
        logger.info("Rejecting friend request with id {}", requestId);

        final FriendRequest friendRequest = getViewedReceivedFriendRequest(requestId);
        friendRequest.setStatus(statusService.getStatus(REJECTED));
        return friendRequestRepository.save(friendRequest);
    }

}
