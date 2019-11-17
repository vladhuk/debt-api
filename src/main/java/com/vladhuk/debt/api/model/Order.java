package com.vladhuk.debt.api.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders_")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Float amount;

    @ManyToOne
    private Status status;

    @ManyToOne
    private User receiver;

    public Order(Float amount, Status status, User receiver) {
        this.amount = amount;
        this.status = status;
        this.receiver = receiver;
    }

}
