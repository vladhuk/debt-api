package com.vladhuk.debt.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "status_")
@Data
public class Status {

    @Id
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StatusName name;

    public enum StatusName {
        SENT, VIEWED, REJECTED, ACCEPTED
    }

}
