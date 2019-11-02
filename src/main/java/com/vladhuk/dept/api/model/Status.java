package com.vladhuk.dept.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
@Data
public class Status {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StatusName statusName;

    public enum StatusName {
        SENT, VIEWED, REJECTED, CONFIRMED
    }

}
