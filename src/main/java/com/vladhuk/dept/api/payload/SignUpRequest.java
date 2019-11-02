package com.vladhuk.dept.api.payload;

import lombok.Data;

@Data
public class SignUpRequest {

    private String name;
    private String username;
    private String password;

}
