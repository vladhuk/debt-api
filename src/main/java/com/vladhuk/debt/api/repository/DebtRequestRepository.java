package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.DebtRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface DebtRequestRepository extends PagingAndSortingRepository<DebtRequest, Long> {

    String findAllByReceiverQuery = "SELECT dr FROM DebtRequest dr INNER JOIN dr.orders ord " +
                                    "WHERE ord.receiver.id = ?1";

    @Query(findAllByReceiverQuery)
    List<DebtRequest> findAllByReceiverId(Long receiverId);

    @Query(findAllByReceiverQuery)
    List<DebtRequest> findAllByReceiverId(Long receiverId, Pageable pageable);

    List<DebtRequest> findAllBySenderId(Long senderId);

    List<DebtRequest> findAllBySenderId(Long senderId, Pageable pageable);

}
