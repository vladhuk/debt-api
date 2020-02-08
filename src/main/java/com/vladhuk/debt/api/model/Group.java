package com.vladhuk.debt.api.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "groups_")
@Data
public class Group {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @ManyToOne
    private User owner;

    @ManyToMany
    @JoinTable(name = "groups_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> members = new ArrayList<>();
}
