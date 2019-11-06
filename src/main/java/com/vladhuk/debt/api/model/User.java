package com.vladhuk.debt.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        })
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class User extends DateAudit {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String username;
    @JsonIgnore
    private String password;

    @ManyToMany
    @JoinTable(name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_friend_id"))
    private List<User> friends = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_blacklist",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_black_id"))
    private List<User> blacklist = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

}
