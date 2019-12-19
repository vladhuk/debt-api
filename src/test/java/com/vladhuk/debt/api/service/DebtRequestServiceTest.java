package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.exception.UserNotFriendException;
import com.vladhuk.debt.api.model.*;
import com.vladhuk.debt.api.repository.DebtOrderRepository;
import com.vladhuk.debt.api.repository.DebtRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.vladhuk.debt.api.model.Status.StatusName.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DebtRequestServiceTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DebtService debtService;
    @Autowired
    private DebtRequestService debtRequestService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private FriendService friendService;
    @Autowired
    private DebtRequestRepository debtRequestRepository;
    @Autowired
    private DebtOrderRepository debtOrderRepository;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;
    private Status sentStatus;
    private Status viewedStatus;
    private Status acceptedStatus;
    private Status rejectedStatus;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        sentStatus = statusService.getStatus(SENT);
        viewedStatus = statusService.getStatus(VIEWED);
        acceptedStatus = statusService.getStatus(ACCEPTED);
        rejectedStatus = statusService.getStatus(REJECTED);
    }

    @Test
    public void changeOrderStatusToViewed_When_StatusSentAndNotSent_Expected_SentToViewed() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, viewedStatus, registeredTestUser1),
                new Order(0f, acceptedStatus, registeredTestUser1),
                new Order(0f, rejectedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setOrders(orders);

        final List<Order> actualOrders =
                debtRequestService.changeOrderStatusToViewed(Collections.singletonList(debtRequest)).get(0).getOrders();

        assertEquals(VIEWED, actualOrders.get(0).getStatus().getName());
        assertEquals(VIEWED, actualOrders.get(1).getStatus().getName());
        assertEquals(VIEWED, actualOrders.get(2).getStatus().getName());
        assertEquals(ACCEPTED, actualOrders.get(3).getStatus().getName());
        assertEquals(REJECTED, actualOrders.get(4).getStatus().getName());
    }

    @Test
    public void sendDebtRequest_When_UserFriend_Expected_Request() {
        friendService.createFriendship(registeredTestUser2.getId());

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setSender(registeredTestUser1);
        debtRequest.setOrders(Collections.singletonList(new Order(0f, sentStatus, registeredTestUser2)));

        final DebtRequest savedDebtRequest = debtRequestService.sendDebtRequest(debtRequest);

        assertEquals(registeredTestUser1, savedDebtRequest.getSender());
        assertEquals(sentStatus, savedDebtRequest.getStatus());

        debtRequest.getOrders().get(0).setId(savedDebtRequest.getOrders().get(0).getId());
        assertEquals(debtRequest.getOrders(), savedDebtRequest.getOrders());
    }

    @Test
    public void sendDebtRequest_When_UserNotFriend_Expected_DebtRequestException() {
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setSender(registeredTestUser1);
        debtRequest.setOrders(Collections.singletonList(new Order(0f, sentStatus, registeredTestUser2)));

        assertThrows(UserNotFriendException.class, () -> debtRequestService.sendDebtRequest(debtRequest));
    }

    @Test
    public void changeStatusToAcceptedIfAllOrdersAccepted_When_AllAccepted_Expected_RequestStatusAccepted() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, acceptedStatus, registeredTestUser1),
                new Order(0f, acceptedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setOrders(orders);

        debtRequestService.changeStatusToAcceptedIfAllOrdersAccepted(debtRequest);

        assertEquals(ACCEPTED, debtRequest.getStatus().getName());
    }

    @Test
    public void changeStatusToAcceptedIfAllOrdersAccepted_When_NotAllAccepted_Expected_RequestStatusSent() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, viewedStatus, registeredTestUser1),
                new Order(0f, acceptedStatus, registeredTestUser1),
                new Order(0f, rejectedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setOrders(orders);

        debtRequestService.changeStatusToAcceptedIfAllOrdersAccepted(debtRequest);

        assertEquals(SENT, debtRequest.getStatus().getName());
    }

    @Test
    public void acceptDebtRequestAndUpdateBalance_When_AllAcceptedAndDebtNotExist_Expected_CreateDebt() {
        friendService.createFriendship(registeredTestUser2.getId());

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        debtOrderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest acceptedRequest = debtRequestService.acceptDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(3f, debt.getBalance());
        assertEquals(registeredTestUser2, debt.getCreditor());
        assertEquals(registeredTestUser1, debt.getBorrower());
    }

    @Test
    public void acceptDebtRequestAndUpdateBalance_When_AllAcceptedAndDebtExist_Expected_AddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        debtService.createDebt(new Debt(registeredTestUser2, registeredTestUser1, 5f));

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        debtOrderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest acceptedRequest = debtRequestService.acceptDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(8f, debt.getBalance());
        assertEquals(registeredTestUser2, debt.getCreditor());
        assertEquals(registeredTestUser1, debt.getBorrower());
    }

    @Test
    public void acceptDebtRequestAndUpdateBalance_When_AllAcceptedAndDebtExistAndCurrentUserBorrower_Expected_AddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        debtService.createDebt(new Debt(registeredTestUser1, registeredTestUser2, 5f));

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        debtOrderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest acceptedRequest = debtRequestService.acceptDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(ACCEPTED, acceptedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(2f, debt.getBalance());
        assertEquals(registeredTestUser1, debt.getCreditor());
        assertEquals(registeredTestUser2, debt.getBorrower());
    }

    @Test
    public void acceptDebtRequestAndUpdateBalance_When_NotAllOrdersAcceptedAndDebtExist_Expected_NotAddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        final Order order1 = new Order(3f, viewedStatus, registeredTestUser1);
        debtOrderRepository.save(order1);
        final Order order2 = new Order(3f, viewedStatus, authenticationService.registerUser(new User("qqq", "qqq", "qqq")));
        debtOrderRepository.save(order2);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser1);
        debtRequest.setOrders(new ArrayList<>(Arrays.asList(order1, order2)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest acceptedRequest = debtRequestService.acceptDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(SENT, acceptedRequest.getStatus().getName());

        assertThrows(ResourceNotFoundException.class, () -> debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId()));
    }

    @Test
    public void rejectDebtRequestAndUpdateBalance_When_OrderStatusViewed_Expected_RejectedRequest() {
        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        debtOrderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest acceptedRequest = debtRequestService.rejectDebtRequest(savedDebtRequest.getId());

        assertEquals(REJECTED, acceptedRequest.getStatus().getName());
    }

}
