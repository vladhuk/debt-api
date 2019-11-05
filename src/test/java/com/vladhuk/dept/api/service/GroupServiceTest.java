package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Group;
import com.vladhuk.dept.api.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private Group testGroup;

    @BeforeEach
    private void setUp() {
        testGroup = new Group();
        testGroup.setTitle("Group name");
    }

    @Test
    public void addMember_When_CurrentUserOwnerAndGroupExistsAndNewMemberExists_Expected_GroupWithNewMember() {

    }

}
