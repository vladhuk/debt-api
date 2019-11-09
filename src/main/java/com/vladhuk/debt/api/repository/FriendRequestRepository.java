package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.FriendRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends PagingAndSortingRepository<FriendRequest, Long> {

    List<FriendRequest> findAllByReceiverId(Long receiverId);

    List<FriendRequest> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<FriendRequest> findAllBySenderId(Long senderId);

    List<FriendRequest> findAllBySenderId(Long senderId, Pageable pageable);

    Optional<FriendRequest> findByIdAndReceiverId(Long id, Long receiverId);

    Long countAllByReceiverIdAndStatusId(Long receiverId, Long statusId);

}
