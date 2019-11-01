package com.vladhuk.dept.api.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class DebtRequest extends Request {

    @OneToMany
    private List<Order> orders;

}
