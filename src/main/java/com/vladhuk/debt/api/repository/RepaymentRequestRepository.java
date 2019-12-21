package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.RepaymentRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface RepaymentRequestRepository extends PagingAndSortingRepository<RepaymentRequest, Long> {

    List<RepaymentRequest> findAllByOrderReceiverId(Long receiverId);

    List<RepaymentRequest> findAllByOrderReceiverId(Long receiverId, Pageable pageable);

    List<RepaymentRequest> findAllBySenderId(Long senderId);

    List<RepaymentRequest> findAllBySenderIdAndOrderReceiverIdAndStatusId(Long senderId, Long receiverId, Long statusId);

    List<RepaymentRequest> findAllBySenderId(Long senderId, Pageable pageable);

    Optional<RepaymentRequest> findByIdAndSenderId(Long id, Long senderId);

    Long countAllByOrderReceiverIdAndStatusId(Long receiverId, Long statusId);

    Optional<RepaymentRequest> findByIdAndOrderReceiverIdAndStatusId(Long id, Long receiverId, Long statusId);

}
