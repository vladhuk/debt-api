package com.vladhuk.dept.api.entity;

import com.vladhuk.dept.api.util.Status;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Order {

    @Id
    private Long id;
    private User sender;
    private User receiver;
    private Float amount;
    private Status status;

}
