package com.vladhuk.dept.api.model;

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

    @OneToOne
    private Status status;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

}
