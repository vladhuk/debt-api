package com.vladhuk.debt.api.controller;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.payload.JwtAuthenticationResponse;
import com.vladhuk.debt.api.payload.LoginRequest;
import com.vladhuk.debt.api.payload.SignUpRequest;
import com.vladhuk.debt.api.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        final String jwt = authenticationService.authenticateAndGetToken(
                loginRequest.getUsername(), loginRequest.getPassword()
        );

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        authenticationService.registerUser(
                new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getPassword())
        );

        final String jwt = authenticationService.authenticateAndGetToken(
                signUpRequest.getUsername(), signUpRequest.getPassword()
        );

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

}
