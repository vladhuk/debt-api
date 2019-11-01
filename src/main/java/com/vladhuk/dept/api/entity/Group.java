package com.vladhuk.dept.api.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.List;

@Entity
@Data
public class Group {

    @Id
    private Long id;
    private String title;

    @ManyToOne
    private User owner;
    private List<User> members;
}
