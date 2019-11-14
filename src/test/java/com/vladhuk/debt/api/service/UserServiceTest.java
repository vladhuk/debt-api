package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.BadRequestException;
import com.vladhuk.debt.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;

    private User testUser = new User("Name1", "Username1", "Password1");
    private User registeredTestUser;

    @BeforeEach
    private void setUp() {
        registeredTestUser = authenticationService.registerUser(testUser);
    }

    @Test
    public void getUser_ByUser_When_IdNotNullAndUsernameNull_Expected_User() {
        final User user = new User();
        user.setUsername(registeredTestUser.getUsername());

        assertEquals(registeredTestUser, userService.getUser(user));
    }

    @Test
    public void getUser_ByUser_When_IdNullAndUsernameNotNull_Expected_User() {
        final User user = new User();
        user.setId(registeredTestUser.getId());

        assertEquals(registeredTestUser, userService.getUser(user));
    }

    @Test
    public void getUser_ByUser_When_IdNullAndUsernameNull_Expected_NotFoundException() {
        final User user = new User();
        assertThrows(BadRequestException.class, () -> userService.getUser(user));
    }

}
