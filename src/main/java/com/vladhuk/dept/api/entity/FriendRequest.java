package com.vladhuk.dept.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class FriendRequest extends Request {

    @OneToOne
    private User receiver;

}
