package com.vladhuk.dept.api.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Debt {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User creditor;
    @ManyToOne
    private User borrower;
    private Float balance;

}
