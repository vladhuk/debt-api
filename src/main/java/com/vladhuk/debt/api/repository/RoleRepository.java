package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByName(Role.RoleName roleName);
}
