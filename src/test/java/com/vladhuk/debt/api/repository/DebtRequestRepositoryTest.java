package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.DebtRequest;
import com.vladhuk.debt.api.model.Order;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class DebtRequestRepositoryTest {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private DebtRequestRepository debtRequestRepository;
    @Autowired
    private OrderRepository orderRepository;

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
    public void findAllByReceiverId_When_RequestExist_Expected_ListWithRequest() {
        final Order orderWithoutCurrentUser = orderRepository.save(new Order(0f, null, registeredTestUser2));
        final Order orderWithCurrentUser = orderRepository.save(new Order(0f, null, registeredTestUser1));

        DebtRequest requestWithoutCurrentUserReceiver = new DebtRequest();
        requestWithoutCurrentUserReceiver.setSender(registeredTestUser1);
        requestWithoutCurrentUserReceiver.setOrders(new ArrayList<>(Collections.singletonList(orderWithoutCurrentUser)));
        debtRequestRepository.save(requestWithoutCurrentUserReceiver);

        DebtRequest requestWithCurrentUserReceiver = new DebtRequest();
        requestWithoutCurrentUserReceiver.setSender(registeredTestUser1);
        requestWithoutCurrentUserReceiver.setOrders(new ArrayList<>(Collections.singletonList(orderWithCurrentUser)));
        debtRequestRepository.save(requestWithCurrentUserReceiver);

        final List<DebtRequest> fetchedRequests = debtRequestRepository.findAllByReceiverId(registeredTestUser1.getId());

        assertEquals(1, fetchedRequests.size());
        assertEquals(registeredTestUser1, fetchedRequests.get(0).getOrders().get(0).getReceiver());
    }

}
