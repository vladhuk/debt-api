package com.vladhuk.dept.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Entity
@Table(name = "debts")
@Data
@EqualsAndHashCode(callSuper = true)
public class Debt extends DateAudit {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User creditor;

    @ManyToOne
    private User borrower;

    private Float balance;

}
