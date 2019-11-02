package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Role;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.repository.RoleRepository;
import com.vladhuk.dept.api.repository.UserRepository;
import com.vladhuk.dept.api.security.UserPrincipal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import static com.vladhuk.dept.api.model.Role.RoleName.USER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthenticationServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RoleRepository roleRepository;

    private User testUser = new User("Name", "Username", "Password");

    @Test
    public void isUsernameExist_When_UsernameExist_Expected_True() {
        userRepository.save(testUser);
        assertTrue(authenticationService.isUsernameExist(testUser.getUsername()));
    }

    @Test
    public void isUsernameExist_When_UsernameNotExist_Expected_False() {
        assertFalse(authenticationService.isUsernameExist(testUser.getUsername()));
    }

    @Test
    public void registerUser_When_UserIsCorrect_Expected_EncodedPasswordAndUserRole() {
        final User registeredUser = authenticationService.registerUser(testUser);
        final Optional<Role> userRole = roleRepository.findByName(USER);

        if (!userRole.isPresent()) {
            fail();
        }

        assertEquals(testUser.getName(), registeredUser.getName());
        assertEquals(testUser.getUsername(), registeredUser.getUsername());
        assertNotEquals(registeredUser.getPassword(), testUser.getPassword());
        assertEquals(1, registeredUser.getRoles().size());
        assertEquals(Collections.singleton(userRole.get()), registeredUser.getRoles());
    }

    @Test
    public void authenticateAndGetToken_When_UsernameAndPasswordCorrect_Expected_CorrectAuthenticationAndToken() {
        authenticationService.registerUser(testUser);
        final String jwt = authenticationService.authenticateAndGetToken(testUser.getUsername(), testUser.getPassword());

        assertTrue(jwt.length() > 0);

        final UserPrincipal user = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getUsername(), user.getUsername());
    }

    @Test
    public void authenticateAndGetToken_When_UsernameAndPasswordNotCorrect_Expected_Exception() {
        assertThrows(AuthenticationException.class,
                     () -> authenticationService.authenticateAndGetToken(testUser.getUsername(), testUser.getPassword()));
    }

}
