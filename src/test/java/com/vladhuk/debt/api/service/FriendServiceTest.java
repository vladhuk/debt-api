package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
    private DebtRepository debtRepository;

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
    public void deleteFriend_WhenExistsDebtWithFriend_Expected_False() {
        final Debt debt = new Debt();
        debt.setCreditor(registeredTestUser1);
        debt.setBorrower(registeredTestUser2);
        debt.setBalance(1.0f);
        debtRepository.save(debt);

        registeredTestUser1.getFriends().add(registeredTestUser2);
        final User userWithFriend = userService.updateUser(registeredTestUser1);

        assertFalse(friendService.deleteFriend(registeredTestUser2.getId()));
        assertEquals(1, userWithFriend.getFriends().size());
    }

    @Test
    public void deleteFriend_WhenNotExistsDebtWithFriend_Expected_True() {

    }


}
