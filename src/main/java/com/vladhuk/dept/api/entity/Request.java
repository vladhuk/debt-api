package com.vladhuk.dept.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladhuk.dept.api.util.Status;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public abstract class Request {

    @Id
    private Long id;
    private User sender;
    private String comment;
    private Status status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime sendingDate;

}
