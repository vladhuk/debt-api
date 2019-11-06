package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "requests_debt")
@Data
@EqualsAndHashCode(callSuper = true)
public class DebtRequest extends Request {

    @OneToMany
    @JoinTable(name = "requests_debt_orders",
            joinColumns = @JoinColumn(name = "request_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;

}
