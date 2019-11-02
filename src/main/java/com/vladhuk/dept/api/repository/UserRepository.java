package com.vladhuk.dept.api.repository;

import com.vladhuk.dept.api.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

}
