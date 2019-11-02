package com.vladhuk.dept.api.model;

import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
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
