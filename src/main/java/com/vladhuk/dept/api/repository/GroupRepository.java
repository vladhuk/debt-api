package com.vladhuk.dept.api.repository;

import com.vladhuk.dept.api.model.Group;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface GroupRepository extends PagingAndSortingRepository<Group, Long> {

    List<Group> findByOwnerId(Long id, Pageable pageable);

}
