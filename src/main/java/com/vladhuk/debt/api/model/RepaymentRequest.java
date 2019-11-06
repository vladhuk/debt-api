package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requests_repayment")
@Data
@EqualsAndHashCode(callSuper = true)
public class RepaymentRequest extends Request {

    @OneToOne
    private Order order;

}
