package com.vladhuk.dept.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class RepaymentRequest extends Request {

    private Order order;

}
