package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Order;
import org.springframework.data.repository.CrudRepository;

public interface RepaymentOrderRepository extends CrudRepository<Order, Long> {
}
