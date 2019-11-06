package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Request extends DateAudit {

    @Id
    @GeneratedValue
    private Long id;

    private String comment;

    @ManyToOne
    private User sender;

    @OneToOne
    private Status status;

}
