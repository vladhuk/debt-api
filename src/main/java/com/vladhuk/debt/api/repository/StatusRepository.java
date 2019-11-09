package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Status;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatusRepository extends CrudRepository<Status, Long> {

    Optional<Status> findByName(Status.StatusName statusName);

}
