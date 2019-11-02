package com.vladhuk.dept.api.repository;

import com.vladhuk.dept.api.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName roleName);
}
