package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT ord FROM DebtRequest dr INNER JOIN dr.orders ord " +
            "WHERE dr.id = ?1 AND ord.receiver.id = ?2 AND ord.status.id = ?3")
    Optional<Order> findByDebtRequestIdAndReceiverIdAndStatusId(Long debtRequestId, Long receiverId, Long statusId);

    Long countAllByReceiverIdAndStatusId(Long receiverId, Long statusId);

}
