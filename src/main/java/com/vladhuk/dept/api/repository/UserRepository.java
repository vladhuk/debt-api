package com.vladhuk.dept.api.repository;

import com.vladhuk.dept.api.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {
}
