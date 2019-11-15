package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.Debt;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class DebtRepositoryTest {

    @Autowired
    private DebtRepository debtRepository;
    @Autowired
    private AuthenticationService authenticationService;

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
    public void deleteById_And_CreditorOrBorrowerId_When_CurrentUserIsCreditor_Expected_Deleted() {
        final Debt debt = debtRepository.save(new Debt(registeredTestUser1, registeredTestUser2, 0.0f));
        debtRepository.deleteById_And_CreditorOrBorrowerId(debt.getId(), registeredTestUser1.getId());
        assertTrue(debtRepository.findById(debt.getId()).isEmpty());
    }

    @Test
    public void deleteById_And_CreditorOrBorrowerId_When_CurrentUserIsBorrower_Expected_Deleted() {
        final Debt debt = debtRepository.save(new Debt(registeredTestUser2, registeredTestUser1, 0.0f));
        debtRepository.deleteById_And_CreditorOrBorrowerId(debt.getId(), registeredTestUser1.getId());
        assertTrue(debtRepository.findById(debt.getId()).isEmpty());
    }

    @Test
    public void deleteById_And_CreditorOrBorrowerId_When_CurrentUserNotRelatedToDebt_Expected_NotDeleted() {
        final User registeredTestUser = authenticationService.registerUser(new User("n", "u", "p"));
        final Debt debt = debtRepository.save(new Debt(registeredTestUser, registeredTestUser2, 0.0f));
        debtRepository.deleteById_And_CreditorOrBorrowerId(debt.getId(), registeredTestUser1.getId());
        assertTrue(debtRepository.findById(debt.getId()).isPresent());
    }

}
