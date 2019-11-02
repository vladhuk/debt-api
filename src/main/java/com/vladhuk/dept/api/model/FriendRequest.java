package com.vladhuk.dept.api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "requests_friend")
@Data
@EqualsAndHashCode(callSuper = true)
public class FriendRequest extends Request {

    @OneToOne
    private User receiver;

}
