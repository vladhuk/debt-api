package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

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

    @ManyToOne
    private Status status;

}
