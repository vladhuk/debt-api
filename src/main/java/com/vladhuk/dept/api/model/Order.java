package com.vladhuk.dept.api.model;

import com.vladhuk.dept.api.util.Status;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity(name = "[order]")
@Data
public class Order {

    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    private User sender;
    @ManyToOne
    private User receiver;
    private Float amount;
    private Status status;

}
