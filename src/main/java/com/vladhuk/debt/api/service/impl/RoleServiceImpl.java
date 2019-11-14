package com.vladhuk.debt.api.service.impl;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.Role;
import com.vladhuk.debt.api.repository.RoleRepository;
import com.vladhuk.debt.api.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRole(Role.RoleName roleName) {
        final Optional<Role> role = roleRepository.findByName(roleName);

        if (role.isEmpty()) {
            logger.error("Role with name {} not founded", roleName);
            throw new ResourceNotFoundException("Role", "name", roleName);
        }

        return role.get();
    }

}
