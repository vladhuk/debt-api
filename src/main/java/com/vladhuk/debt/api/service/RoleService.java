package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Role;

public interface RoleService {

    Role getRole(Role.RoleName roleName);

}
