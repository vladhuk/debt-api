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
public class DebtServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DebtService debtService;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;
    private Debt testNotSavedDebt;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        testNotSavedDebt = new Debt(registeredTestUser1, registeredTestUser2, 0f);
    }

    @Test
    public void isExistsDebtWithUser_When_DebtExist_Expected_True() {
        debtService.createDebt(testNotSavedDebt);

        assertTrue(debtService.isExistsDebtWithUser(registeredTestUser2.getId()));
    }

    @Test
    public void isExistsDebtWithUser_When_DebtNotExist_Expected_False() {
        assertFalse(debtService.isExistsDebtWithUser(registeredTestUser2.getId()));
    }

    @Test
    public void addToBalance_When_BalanceExist_Expect_CorrectAdd() {
        final Float startBalance = 5f;
        final Float diff = 5.4f;
        final Float expectedBalance = 10.4f;

        testNotSavedDebt.setBalance(startBalance);
        final Debt createdDebt = debtService.createDebt(testNotSavedDebt);

        final Debt updatedDebt = debtService.addToBalance(createdDebt.getId(), diff);

        assertEquals(expectedBalance, updatedDebt.getBalance());
    }

    @Test
    public void isBalanceZero_When_BalanceVerySmall_Expected_True() {
        final Float verySmallBalance1 = 1e-5f;
        final Float verySmallBalance2 = 0.004f;

        testNotSavedDebt.setBalance(verySmallBalance1);
        assertTrue(debtService.isBalanceZero(testNotSavedDebt));

        testNotSavedDebt.setBalance(verySmallBalance2);
        assertTrue(debtService.isBalanceZero(testNotSavedDebt));
    }

}
