package com.vladhuk.debt.api.repository;

import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.service.AuthenticationService;
import com.vladhuk.debt.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserService userService;

    private User testUser = new User("Name", "Username", "Password");

    @Test
    public void findAllFriendsByUserId_When_UserSignedIn_Expected_CorrectList() {
        final User registeredUser = authenticationService.registerUser(testUser);
        authenticationService.authenticateAndGetToken(testUser.getUsername(), testUser.getPassword());

        final List<User> testUsers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            testUsers.add(authenticationService.registerUser(new User("n" + i, "u" + i, "p" + i)));
        }

        final List<User> futureFriends = Arrays.asList(testUsers.get(0), testUsers.get(3));
        registeredUser.getFriends().addAll(futureFriends);
        userService.updateUser(registeredUser);

        final List<User> fetchedFriends = friendRepository.findAllFriendsByUserId(registeredUser.getId(), PageRequest.of(0, 999));

        assertEquals(futureFriends, fetchedFriends);
    }
}
