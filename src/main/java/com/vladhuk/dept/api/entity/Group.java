package com.vladhuk.dept.api.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity(name = "[group]")
@Data
public class Group {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @ManyToOne
    private User owner;
    @OneToMany
    private List<User> members;
}
