package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.RepaymentRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.*;
import com.vladhuk.debt.api.repository.RepaymentOrderRepository;
import com.vladhuk.debt.api.repository.RepaymentRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.vladhuk.debt.api.model.Status.StatusName.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class RepaymentRequestServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private RepaymentRequestService repaymentRequestService;
    @Autowired
    private RepaymentRequestRepository repaymentRequestRepository;
    @Autowired
    private RepaymentOrderRepository repaymentOrderRepository;
    @Autowired
    private StatusService statusService;
    @Autowired
    private DebtService debtService;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;
    private Status sentStatus;
    private Status viewedStatus;
    private Status confirmedStatus;
    private Status rejectedStatus;
    private Debt testNotSavedDebt;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        sentStatus = statusService.getStatus(SENT);
        viewedStatus = statusService.getStatus(VIEWED);
        confirmedStatus = statusService.getStatus(CONFIRMED);
        rejectedStatus = statusService.getStatus(REJECTED);

        testNotSavedDebt = new Debt(registeredTestUser1, registeredTestUser2, 5f);
    }

    @Test
    public void sendRepaymentRequest_When_UsersHaveDebt_Expected_RepaymentRequest() {
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRepaymentRequest = new RepaymentRequest();
        notSentRepaymentRequest.setOrder(new Order(5f, sentStatus, registeredTestUser2));
        notSentRepaymentRequest.setSender(registeredTestUser1);
        notSentRepaymentRequest.setStatus(sentStatus);

        final RepaymentRequest sentRepaymentRequest = repaymentRequestService.sendRepaymentRequest(notSentRepaymentRequest);

        assertEquals(registeredTestUser1, sentRepaymentRequest.getSender());
        assertEquals(registeredTestUser2, sentRepaymentRequest.getOrder().getReceiver());
        assertEquals(sentStatus, sentRepaymentRequest.getStatus());
        assertEquals(5f, sentRepaymentRequest.getOrder().getAmount());
    }

    @Test
    public void sendRepaymentRequest_When_UsersNotHaveDebt_Expected_RepaymentRequestException() {
        final RepaymentRequest notSentRepaymentRequest = new RepaymentRequest();
        notSentRepaymentRequest.setOrder(new Order(5f, sentStatus, registeredTestUser2));
        notSentRepaymentRequest.setSender(registeredTestUser1);
        notSentRepaymentRequest.setStatus(sentStatus);

        assertThrows(RepaymentRequestException.class,
                     () -> repaymentRequestService.sendRepaymentRequest(notSentRepaymentRequest));
    }

    @Test
    public void confirmRepaymentRequestAndUpdateBalance_When_ExistNotViewedRequest_Expected_ResourceNotFoundException() {
        final RepaymentRequest notSentRepaymentRequest = new RepaymentRequest();
        notSentRepaymentRequest.setOrder(
                repaymentOrderRepository.save(new Order(5f, sentStatus, registeredTestUser2))
        );
        notSentRepaymentRequest.setSender(registeredTestUser1);
        notSentRepaymentRequest.setStatus(sentStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRepaymentRequest);

        assertThrows(ResourceNotFoundException.class,
                     () -> repaymentRequestService.confirmRepaymentRequestAndUpdateBalance(savedRequest.getId()));
    }

    @Test
    public void confirmRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserCreditor_Expected_PlusDebtBalance() {
        testNotSavedDebt.setCreditor(registeredTestUser1);
        testNotSavedDebt.setBorrower(registeredTestUser2);
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRequest = new RepaymentRequest();
        notSentRequest.setOrder(
                repaymentOrderRepository.save(new Order(3f, sentStatus, registeredTestUser1))
        );
        notSentRequest.setSender(registeredTestUser2);
        notSentRequest.setStatus(viewedStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRequest);

        final RepaymentRequest confirmedRequest = repaymentRequestService.confirmRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId());

        assertEquals(2f, debt.getBalance());
    }

    @Test
    public void confirmRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserBorrower_Expected_MinusDebtBalance() {
        testNotSavedDebt.setCreditor(registeredTestUser2);
        testNotSavedDebt.setBorrower(registeredTestUser1);
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRequest = new RepaymentRequest();
        notSentRequest.setOrder(
                repaymentOrderRepository.save(new Order(3f, sentStatus, registeredTestUser1))
        );
        notSentRequest.setSender(registeredTestUser2);
        notSentRequest.setStatus(viewedStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRequest);

        final RepaymentRequest confirmedRequest = repaymentRequestService.confirmRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId());

        assertEquals(8f, debt.getBalance());
    }

    @Test
    public void confirmRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserBorrowerAndBalanceWillBeZero_Expected_DeleteDebt() {
        testNotSavedDebt.setCreditor(registeredTestUser2);
        testNotSavedDebt.setBorrower(registeredTestUser1);
        testNotSavedDebt.setBalance(-5f);
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRequest = new RepaymentRequest();
        notSentRequest.setOrder(
                repaymentOrderRepository.save(new Order(5f, sentStatus, registeredTestUser1))
        );
        notSentRequest.setSender(registeredTestUser2);
        notSentRequest.setStatus(viewedStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRequest);

        final RepaymentRequest confirmedRequest = repaymentRequestService.confirmRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        assertThrows(ResourceNotFoundException.class,
                     () -> debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId()));
    }

    @Test
    public void rejectRepaymentRequest_When_RequestViewed_Expected_Rejected() {
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRequest = new RepaymentRequest();
        notSentRequest.setOrder(
                repaymentOrderRepository.save(new Order(5f, sentStatus, registeredTestUser1))
        );
        notSentRequest.setSender(registeredTestUser2);
        notSentRequest.setStatus(viewedStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRequest);

        final RepaymentRequest confirmedRequest = repaymentRequestService.rejectRepaymentRequest(savedRequest.getId());

        assertEquals(REJECTED, confirmedRequest.getStatus().getName());
    }

}
