package com.vladhuk.dept.api.payload;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;

}
