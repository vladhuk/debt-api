package com.vladhuk.dept.api.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
public class User {

    @Id
    private Long id;
    private String name;
    private String email;

    @OneToMany
    private List<User> friends;
    @OneToMany
    private List<User> blacklist;
    @OneToMany
    private List<Debt> debts;
    @OneToMany
    private List<Group> groups;

}
