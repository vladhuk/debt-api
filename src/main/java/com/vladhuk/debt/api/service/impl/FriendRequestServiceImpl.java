package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.model.FriendRequest;
import com.vladhuk.debt.api.model.Status;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.FriendRequestRepository;
import com.vladhuk.debt.api.service.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vladhuk.debt.api.model.Status.StatusName.*;

@Service
@Transactional
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final AuthenticationService authenticationService;
    private final StatusService statusService;
    private final UserService userService;
    private final FriendService friendService;

    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, AuthenticationService authenticationService, StatusService statusService, UserService userService, FriendService friendService) {
        this.friendRequestRepository = friendRequestRepository;
        this.authenticationService = authenticationService;
        this.statusService = statusService;
        this.userService = userService;
        this.friendService = friendService;
    }

    @Override
    public List<FriendRequest> getAllSentFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        return friendRequestRepository.findAllBySenderId(currentUserId);
    }

    @Override
    public List<FriendRequest> getSentFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());
        return friendRequestRepository.findAllBySenderId(currentUserId, pageable);
    }

    @Override
    public List<FriendRequest> changeStatusToViewed(List<FriendRequest> requests) {
        final Status viewedStatus = statusService.getStatus(VIEWED);
        return requests.stream()
                .peek(request -> {
                    if (request.getStatus().getName() == SENT) {
                        request.setStatus(viewedStatus);
                        friendRequestRepository.save(request);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FriendRequest> getAllReceivedFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        return changeStatusToViewed(
                friendRequestRepository.findAllByReceiverId(currentUserId)
        );
    }

    @Override
    public List<FriendRequest> getReceivedFriendRequestsPage(Integer pageNumber, Integer pageSize) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("updated_at").descending());
        return changeStatusToViewed(
                friendRequestRepository.findAllByReceiverId(currentUserId, pageable)
        );
    }

    @Override
    public Long countNewReceivedFriendRequests() {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final Long statusId = statusService.getStatus(SENT).getId();
        return friendRequestRepository.countAllByReceiverIdAndStatusId(currentUserId, statusId);
    }

    @Override
    public Optional<FriendRequest> sendFriendRequest(FriendRequest friendRequest) {
        final User receiver = userService.getUser(friendRequest.getReceiver());

        if (friendService.isFriend(receiver.getId())) {
            final FriendRequest requestForSave = new FriendRequest();
            requestForSave.setSender(authenticationService.getCurrentUser());
            requestForSave.setReceiver(receiver);
            requestForSave.setComment(friendRequest.getComment());
            requestForSave.setStatus(statusService.getStatus(SENT));

            return Optional.of(friendRequestRepository.save(requestForSave));
        }
        return Optional.empty();
    }

    // TODO: Make optional
    @Override
    public FriendRequest acceptFriendRequest(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final FriendRequest friendRequest =
                friendRequestRepository.findByIdAndReceiverId(requestId, currentUserId).get();

        friendRequest.setStatus(statusService.getStatus(CONFIRMED));
        friendService.createFriendship(friendRequest.getSender().getId());
        return friendRequestRepository.save(friendRequest);
    }

    // TODO: Make optional
    @Override
    public FriendRequest rejectFriendRequest(Long requestId) {
        final Long currentUserId = authenticationService.getCurrentUser().getId();
        final FriendRequest friendRequest =
                friendRequestRepository.findByIdAndReceiverId(requestId, currentUserId).get();

        friendRequest.setStatus(statusService.getStatus(REJECTED));
        return friendRequestRepository.save(friendRequest);
    }

}
