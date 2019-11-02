package com.vladhuk.dept.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity(name = "roles")
@Data
public class Role {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private RoleName roleName;

    public enum RoleName {
        USER
    }

}
