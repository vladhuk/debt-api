package com.vladhuk.dept.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class RepaymentRequest extends Request {

    @OneToOne
    private Order order;

}
