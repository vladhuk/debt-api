package com.vladhuk.dept.api.controller;

import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.payload.ApiResponse;
import com.vladhuk.dept.api.payload.JwtAuthenticationResponse;
import com.vladhuk.dept.api.payload.LoginRequest;
import com.vladhuk.dept.api.payload.SignUpRequest;
import com.vladhuk.dept.api.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
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
        if (authenticationService.isUsernameExist(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
        }

        final User newUser = new User(
                signUpRequest.getName(),
                signUpRequest.getUsername(),
                signUpRequest.getPassword()
        );
        authenticationService.registerUser(newUser);

        final String jwt = authenticationService.authenticateAndGetToken(
                newUser.getUsername(), newUser.getPassword()
        );

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @GetMapping("/logout")
    public void logout() {
        SecurityContextHolder.clearContext();
    }

}
