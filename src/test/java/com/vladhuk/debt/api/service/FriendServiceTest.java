package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class FriendServiceTest {

    @Autowired
    private FriendService friendService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DebtService debtService;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());
    }

    @Test
    public void createFriendship_When_UsersNotFriends_Expected_UserHaveFriendAndFriendHaveUser() {
        final User currentUser = friendService.createFriendship(registeredTestUser2.getId());

        assertEquals(1, currentUser.getFriends().size());
        assertEquals(1, userService.getUser(registeredTestUser2).getFriends().size());
    }

    @Test
    public void deleteFriendship_WhenExistsDebtWithFriend_Expected_False() {
        final Debt debt = new Debt(registeredTestUser1, registeredTestUser2, 1.0f);
        debtService.createDebt(debt);

        friendService.createFriendship(registeredTestUser2.getId());

        assertFalse(friendService.deleteFriendship(registeredTestUser2.getId()));
        assertEquals(1, userService.getUser(registeredTestUser1).getFriends().size());
        assertEquals(1, userService.getUser(registeredTestUser2).getFriends().size());
    }

    @Test
    public void deleteFriendship_WhenNotExistsDebtWithFriend_Expected_True() {
        registeredTestUser1.getFriends().add(registeredTestUser2);
        userService.updateUser(registeredTestUser1);

        assertTrue(friendService.deleteFriendship(registeredTestUser2.getId()));
        assertEquals(0, userService.getUser(registeredTestUser1).getFriends().size());
        assertEquals(0, userService.getUser(registeredTestUser2).getFriends().size());
    }

    @Test
    public void isFriend_When_UserFriend_Expected_True() {
        friendService.createFriendship(registeredTestUser2.getId());

        assertTrue(friendService.isFriend(registeredTestUser2.getId()));
    }

    @Test
    public void isFriend_When_UserNotFriend_Expected_False() {
        assertFalse(friendService.isFriend(registeredTestUser2.getId()));
    }

}
