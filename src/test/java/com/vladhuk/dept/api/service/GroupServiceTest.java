package com.vladhuk.dept.api.service;

import com.vladhuk.dept.api.model.Group;
import com.vladhuk.dept.api.model.User;
import com.vladhuk.dept.api.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class GroupServiceTest {

    private User testUser1 = new User("Name1", "Username1", "Password1");
    private User testUser2 = new User("Name2", "Username2", "Password2");
    private User registeredAndAuthTestUser1;
    private User registeredTestUser2;
    private Group testGroup;

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AuthenticationService authenticationService;

    @BeforeEach
    private void setUp() {
        registeredAndAuthTestUser1 = authenticationService.registerUser(testUser1);
        registeredTestUser2 = authenticationService.registerUser(testUser2);

        authenticationService.authenticateAndGetToken(testUser1.getUsername(), testUser1.getPassword());

        testGroup = new Group();
        testGroup.setTitle("Group name");
    }

    @Test
    public void getAllGroups_When_UserHaveGroups_Expected_CorrectList() {
        groupService.createGroup(testGroup);

        testGroup = new Group();
        testGroup.setTitle("group2");
        groupService.createGroup(testGroup);

        SecurityContextHolder.clearContext();
        authenticationService.authenticateAndGetToken(testUser2.getUsername(), testUser2.getPassword());

        testGroup = new Group();
        testGroup.setTitle("group3");
        groupService.createGroup(testGroup);

        testGroup = new Group();
        testGroup.setTitle("group4");
        groupService.createGroup(testGroup);

        final List<Group> groups = groupService.getAllGroups();

        assertEquals(2, groups.size());
        for (Group g : groups) {
            assertTrue(groupService.isCurrentUserOwner(g));
        }
    }

    @Test
    public void getGroupsPage_When_Page2Size1_Expected_CorrectResultWithCorrectOrder() {
        testGroup.setTitle("group1");
        groupService.createGroup(testGroup);

        testGroup = new Group();
        testGroup.setTitle("group3");
        groupService.createGroup(testGroup);

        testGroup = new Group();
        testGroup.setTitle("group2");
        final Group ownGroup = groupService.createGroup(testGroup);

        testGroup = new Group();
        testGroup.setTitle("group4");
        groupService.createGroup(testGroup);

        final List<Group> groups = groupService.getGroupsPage(1, 1);

        assertEquals(1, groups.size());
        assertEquals(ownGroup, groups.get(0));
    }

    @Test
    public void createGroup_When_GroupNotExist_CreateGroup() {
        testGroup.setOwner(registeredAndAuthTestUser1);

        final Group actualGroup = groupService.createGroup(testGroup);

        assertNotNull(actualGroup);

        final Optional<Group> fetchedGroup = groupRepository.findById(actualGroup.getId());

        assertTrue(fetchedGroup.isPresent());
        assertEquals(actualGroup, fetchedGroup.get());
    }

    @Test
    public void addMember_When_CurrentUserOwnerAndGroupExistsAndNewMemberExists_Expected_GroupWithNewMember() {
        final Group group = groupService.createGroup(testGroup);

        final Group groupWithMember = groupService.addMember(group.getId(), registeredTestUser2.getId());
        final Group fetchedGroup = groupService.getGroup(group.getId());

        assertEquals(fetchedGroup, groupWithMember);
    }

    @Test
    public void deleteMember_When_MemberExist_Expected_DeleteMember() {
        Group group = groupService.createGroup(testGroup);
        group = groupService.addMember(group.getId(), registeredTestUser2.getId());

        final Group resultGroup = groupService.deleteMember(group.getId(), registeredTestUser2.getId());

        assertEquals(0, resultGroup.getMembers().size());

        final Optional<Group> fetchedGroup = groupRepository.findById(group.getId());

        assertTrue(fetchedGroup.isPresent());
        assertEquals(fetchedGroup.get(), resultGroup);
    }

    @Test
    public void isCurrentUserOwner_When_UserOwner_Expected_True() {
        final Group group = groupService.createGroup(testGroup);

        assertTrue(groupService.isCurrentUserOwner(group));
    }

    @Test
    public void isCurrentUserOwner_When_UserNotOwner_Expected_False() {
        final Group group = groupService.createGroup(testGroup);

        SecurityContextHolder.clearContext();
        authenticationService.authenticateAndGetToken(testUser2.getUsername(), testUser2.getPassword());

        assertFalse(groupService.isCurrentUserOwner(group));
    }

}
