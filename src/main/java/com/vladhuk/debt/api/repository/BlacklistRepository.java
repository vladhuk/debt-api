package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface BlacklistRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u.blacklist FROM User u WHERE u.id = ?1")
    List<User> findAllBlackUsersByUserId(Long userId, Pageable pageable);

}
