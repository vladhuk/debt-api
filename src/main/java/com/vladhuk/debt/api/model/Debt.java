package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


/**
 * Uses for store the current balance between two users. Two users can have only one debt.
 * If balance is positive, borrower owes money to creditor and if balance is negative,
 * creditor owes money to borrower.
 * Debt creates only with {@link DebtRequest}.
 * The balance of debt can be changed only with {@link DebtRequest} or {@link RepaymentRequest}
 *
 * @author vladhuk
 */
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
