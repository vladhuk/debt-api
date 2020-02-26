package com.vladhuk.debt.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "status_")
@Data
public class Status {

    @Id
    @JsonIgnore
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private StatusName name;

    public enum StatusName {
        SENT, VIEWED, REJECTED, ACCEPTED
    }

}
