package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.DebtRequestException;
import com.vladhuk.debt.api.exception.ResourceNotFoundException;
import com.vladhuk.debt.api.model.*;
import com.vladhuk.debt.api.repository.DebtRequestRepository;
import com.vladhuk.debt.api.repository.OrderRepository;
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
    private OrderRepository orderRepository;

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredTestUser1;
    private User registeredTestUser2;
    private Status sentStatus;
    private Status viewedStatus;
    private Status confirmedStatus;
    private Status rejectedStatus;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        sentStatus = statusService.getStatus(SENT);
        viewedStatus = statusService.getStatus(VIEWED);
        confirmedStatus = statusService.getStatus(CONFIRMED);
        rejectedStatus = statusService.getStatus(REJECTED);
    }

    @Test
    public void changeOrderStatusToViewed_When_StatusSentAndNotSent_Expected_SentToViewed() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, viewedStatus, registeredTestUser1),
                new Order(0f, confirmedStatus, registeredTestUser1),
                new Order(0f, rejectedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setOrders(orders);

        final List<Order> actualOrders =
                debtRequestService.changeOrderStatusToViewed(Collections.singletonList(debtRequest)).get(0).getOrders();

        assertEquals(VIEWED, actualOrders.get(0).getStatus().getName());
        assertEquals(VIEWED, actualOrders.get(1).getStatus().getName());
        assertEquals(VIEWED, actualOrders.get(2).getStatus().getName());
        assertEquals(CONFIRMED, actualOrders.get(3).getStatus().getName());
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

        assertThrows(DebtRequestException.class, () -> debtRequestService.sendDebtRequest(debtRequest));
    }

    @Test
    public void changeStatusToConfirmedIfAllOrdersConfirmed_When_AllConfirmed_Expected_RequestStatusConfirmed() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, confirmedStatus, registeredTestUser1),
                new Order(0f, confirmedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setOrders(orders);

        debtRequestService.changeStatusToConfirmedIfAllOrdersConfirmed(debtRequest);

        assertEquals(CONFIRMED, debtRequest.getStatus().getName());
    }

    @Test
    public void changeStatusToConfirmedIfAllOrdersConfirmed_When_NotAllConfirmed_Expected_RequestStatusSent() {
        final List<Order> orders = Arrays.asList(
                new Order(0f, sentStatus, registeredTestUser1),
                new Order(0f, viewedStatus, registeredTestUser1),
                new Order(0f, confirmedStatus, registeredTestUser1),
                new Order(0f, rejectedStatus, registeredTestUser1)
        );
        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setOrders(orders);

        debtRequestService.changeStatusToConfirmedIfAllOrdersConfirmed(debtRequest);

        assertEquals(SENT, debtRequest.getStatus().getName());
    }

    @Test
    public void confirmDebtRequestAndUpdateBalance_When_AllConfirmedAndDebtNotExist_Expected_CreateDebt() {
        friendService.createFriendship(registeredTestUser2.getId());

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        orderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest confirmedRequest = debtRequestService.confirmDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(3f, debt.getBalance());
        assertEquals(registeredTestUser2, debt.getCreditor());
        assertEquals(registeredTestUser1, debt.getBorrower());
    }

    @Test
    public void confirmDebtRequestAndUpdateBalance_When_AllConfirmedAndDebtExist_Expected_AddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        debtService.createDebt(new Debt(registeredTestUser2, registeredTestUser1, 5f));

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        orderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest confirmedRequest = debtRequestService.confirmDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(8f, debt.getBalance());
        assertEquals(registeredTestUser2, debt.getCreditor());
        assertEquals(registeredTestUser1, debt.getBorrower());
    }

    @Test
    public void confirmDebtRequestAndUpdateBalance_When_AllConfirmedAndDebtExistAndCurrentUserBorrower_Expected_AddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        debtService.createDebt(new Debt(registeredTestUser1, registeredTestUser2, 5f));

        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        orderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest confirmedRequest = debtRequestService.confirmDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(CONFIRMED, confirmedRequest.getStatus().getName());

        final Debt debt = debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId());

        assertEquals(2f, debt.getBalance());
        assertEquals(registeredTestUser1, debt.getCreditor());
        assertEquals(registeredTestUser2, debt.getBorrower());
    }

    @Test
    public void confirmDebtRequestAndUpdateBalance_When_NotAllOrdersConfirmedAndDebtExist_Expected_NotAddToBalance() {
        friendService.createFriendship(registeredTestUser2.getId());

        final Order order1 = new Order(3f, viewedStatus, registeredTestUser1);
        orderRepository.save(order1);
        final Order order2 = new Order(3f, viewedStatus, authenticationService.registerUser(new User("qqq", "qqq", "qqq")));
        orderRepository.save(order2);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser1);
        debtRequest.setOrders(new ArrayList<>(Arrays.asList(order1, order2)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest confirmedRequest = debtRequestService.confirmDebtRequestAndUpdateBalance(savedDebtRequest.getId());

        assertEquals(SENT, confirmedRequest.getStatus().getName());

        assertThrows(ResourceNotFoundException.class, () -> debtService.getDebtWithUsers(registeredTestUser1.getId(), registeredTestUser2.getId()));
    }

    @Test
    public void rejectDebtRequestAndUpdateBalance_When_OrderStatusViewed_Expected_RejectedRequest() {
        final Order order = new Order(3f, viewedStatus, registeredTestUser1);
        orderRepository.save(order);

        final DebtRequest debtRequest = new DebtRequest();
        debtRequest.setStatus(sentStatus);
        debtRequest.setSender(registeredTestUser2);
        debtRequest.setOrders(new ArrayList<>(Collections.singletonList(order)));

        final DebtRequest savedDebtRequest = debtRequestRepository.save(debtRequest);
        final DebtRequest confirmedRequest = debtRequestService.rejectDebtRequest(savedDebtRequest.getId());

        assertEquals(REJECTED, confirmedRequest.getStatus().getName());
    }

}
