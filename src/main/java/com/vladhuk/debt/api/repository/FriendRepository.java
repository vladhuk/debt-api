package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FriendRepository extends PagingAndSortingRepository<User, Long> {

    @Query("SELECT u.friends FROM User u WHERE u.id = ?1")
    List<User> findAllFriendsByUserId(Long userId, Pageable pageable);

}
