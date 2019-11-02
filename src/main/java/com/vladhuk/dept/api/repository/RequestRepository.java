package com.vladhuk.dept.api.repository;

import com.vladhuk.dept.api.model.Request;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {
}
