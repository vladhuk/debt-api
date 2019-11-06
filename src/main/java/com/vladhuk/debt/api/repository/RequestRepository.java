package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
}
