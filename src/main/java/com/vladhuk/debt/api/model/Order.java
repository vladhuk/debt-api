package com.vladhuk.debt.api.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "orders_")
@Data
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    private Float amount;

    @ManyToOne
    private Status status;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

}
