package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.NoOrdersException;
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

import java.util.List;

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
    private Status acceptedStatus;
    private Status rejectedStatus;
    private Debt testNotSavedDebt;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        sentStatus = statusService.getStatus(SENT);
        viewedStatus = statusService.getStatus(VIEWED);
        acceptedStatus = statusService.getStatus(ACCEPTED);
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
    public void sendRepaymentRequest_When_OrderIsNull_Expected_NoOrdersException() {
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRepaymentRequest = new RepaymentRequest();
        notSentRepaymentRequest.setOrder(null);
        notSentRepaymentRequest.setSender(registeredTestUser1);
        notSentRepaymentRequest.setStatus(sentStatus);

        assertThrows(NoOrdersException.class,
                     () -> repaymentRequestService.sendRepaymentRequest(notSentRepaymentRequest));
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
    public void acceptRepaymentRequestAndUpdateBalance_When_ExistNotViewedRequest_Expected_ResourceNotFoundException() {
        final RepaymentRequest notSentRepaymentRequest = new RepaymentRequest();
        notSentRepaymentRequest.setOrder(
                repaymentOrderRepository.save(new Order(5f, sentStatus, registeredTestUser2))
        );
        notSentRepaymentRequest.setSender(registeredTestUser1);
        notSentRepaymentRequest.setStatus(sentStatus);
        final RepaymentRequest savedRequest = repaymentRequestRepository.save(notSentRepaymentRequest);

        assertThrows(ResourceNotFoundException.class,
                     () -> repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(savedRequest.getId()));
    }

    @Test
    public void acceptRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserCreditor_Expected_PlusDebtBalance() {
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

        final RepaymentRequest acceptedRequest = repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId());

        assertEquals(2f, debt.getBalance());
    }

    @Test
    public void acceptRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserBorrower_Expected_MinusDebtBalance() {
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

        final RepaymentRequest acceptedRequest = repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId());

        assertEquals(8f, debt.getBalance());
    }

    @Test
    public void acceptRepaymentRequestAndUpdateBalance_When_TwoViewedRequestAndDebtExistWithCurrentUserBorrowerAndDebtBalanceWillZero_Expected_MinusDebtBalanceAndRejectSecondRequest() {
        testNotSavedDebt.setCreditor(registeredTestUser2);
        testNotSavedDebt.setBorrower(registeredTestUser1);
        debtService.createDebt(testNotSavedDebt);

        final RepaymentRequest notSentRequest1 = new RepaymentRequest();
        notSentRequest1.setOrder(
                repaymentOrderRepository.save(new Order(-5f, sentStatus, registeredTestUser1))
        );
        notSentRequest1.setSender(registeredTestUser2);
        notSentRequest1.setStatus(viewedStatus);
        final RepaymentRequest savedRequest1 = repaymentRequestRepository.save(notSentRequest1);

        final RepaymentRequest notSentRequest2 = new RepaymentRequest();
        notSentRequest2.setOrder(
                repaymentOrderRepository.save(new Order(-5f, sentStatus, registeredTestUser1))
        );
        notSentRequest2.setSender(registeredTestUser2);
        notSentRequest2.setStatus(viewedStatus);
        final RepaymentRequest savedRequest2 = repaymentRequestRepository.save(notSentRequest2);

        final RepaymentRequest acceptedRequest = repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(savedRequest1.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        assertThrows(ResourceNotFoundException.class, () -> debtService.getDebtWithUserAndCurrentUser(registeredTestUser2.getId()));

        assertEquals(REJECTED, savedRequest2.getStatus().getName());
    }

    @Test
    public void acceptRepaymentRequestAndUpdateBalance_When_ViewedRequestAndDebtExistWithCurrentUserBorrowerAndBalanceWillBeZero_Expected_DeleteDebt() {
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

        final RepaymentRequest acceptedRequest = repaymentRequestService.acceptRepaymentRequestAndUpdateBalance(savedRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

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

        final RepaymentRequest acceptedRequest = repaymentRequestService.rejectRepaymentRequest(savedRequest.getId());

        assertEquals(REJECTED, acceptedRequest.getStatus().getName());
    }

    @Test
    public void rejectRepaymentRequestsWithUsersIfStatusSentOrViewed_When_ExistsViewedRequests_Expected_CorrectRejecting() {
        final RepaymentRequest request1 = new RepaymentRequest();
        request1.setOrder(
                repaymentOrderRepository.save(new Order(-5f, sentStatus, registeredTestUser1))
        );
        request1.setSender(registeredTestUser2);
        request1.setStatus(viewedStatus);
        repaymentRequestRepository.save(request1);
        final RepaymentRequest savedRequest1 = repaymentRequestRepository.save(request1);

        final RepaymentRequest request2 = new RepaymentRequest();
        request2.setOrder(
                repaymentOrderRepository.save(new Order(-5f, sentStatus, registeredTestUser1))
        );
        request2.setSender(registeredTestUser2);
        request2.setStatus(acceptedStatus);
        repaymentRequestRepository.save(request2);
        final RepaymentRequest savedRequest2 = repaymentRequestRepository.save(request2);


        final List<RepaymentRequest> repaymentRequests = repaymentRequestService.rejectRepaymentRequestsWithUsersIfStatusSentOrViewed(
                registeredTestUser1.getId(), registeredTestUser2.getId()
        );
        assertEquals(1, repaymentRequests.size());

        assertEquals(REJECTED, savedRequest1.getStatus().getName());
        assertEquals(ACCEPTED, savedRequest2.getStatus().getName());
    }

}
