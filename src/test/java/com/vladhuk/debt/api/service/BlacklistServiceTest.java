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
public class BlacklistServiceTest {

    @Autowired
    private BlacklistService blacklistService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DebtService debtService;
    @Autowired
    private FriendService friendService;

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
    public void addUserToBlacklist_When_UserNotFriend_Expected_True() {
        assertTrue(blacklistService.addUserToBlacklist(registeredTestUser2));

        final User currentUser = authenticationService.getCurrentUser();
        assertEquals(1, currentUser.getBlacklist().size());
        assertEquals(0, currentUser.getFriends().size());
    }

    @Test
    public void addUserToBlacklist_When_UserFriendAndDebtNotExists_Expected_TrueAndDeleteFriendship() {
        friendService.createFriendship(registeredTestUser2.getId());

        assertTrue(blacklistService.addUserToBlacklist(registeredTestUser2));

        final User currentUser = authenticationService.getCurrentUser();
        assertEquals(1, currentUser.getBlacklist().size());
        assertEquals(0, currentUser.getFriends().size());
    }

    @Test
    public void addUserToBlacklist_When_UserFriendAndDebtExists_Expected_FalseAndNotDeletedFriendship() {
        friendService.createFriendship(registeredTestUser2.getId());

        debtService.createDebt(new Debt(registeredTestUser1, registeredTestUser2, 0.0f));

        assertFalse(blacklistService.addUserToBlacklist(registeredTestUser2));

        final User currentUser = authenticationService.getCurrentUser();
        assertEquals(0, currentUser.getBlacklist().size());
        assertEquals(1, currentUser.getFriends().size());
    }

    @Test
    public void deleteUserFromBlackList_When_UserInBlackList_Expected_Deleted() {
        blacklistService.addUserToBlacklist(registeredTestUser2);

        User currentUser = authenticationService.getCurrentUser();
        assertEquals(1, currentUser.getBlacklist().size());

        blacklistService.deleteUserFromBlacklist(registeredTestUser2.getId());

        currentUser = authenticationService.getCurrentUser();
        assertEquals(0, currentUser.getBlacklist().size());
    }

}
