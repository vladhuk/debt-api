package com.vladhuk.debt.api.service;

import com.vladhuk.debt.api.exception.UserNotFriendException;
import com.vladhuk.debt.api.model.Group;
import com.vladhuk.debt.api.model.User;
import com.vladhuk.debt.api.repository.GroupRepository;
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
    private User registeredTestUser1;
    private User registeredTestUser2;
    private Group testGroup;

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private FriendService friendService;

    @BeforeEach
    private void setUp() {
        registeredTestUser1 = authenticationService.registerUser(testUser1);
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
            assertEquals(g.getOwner(), authenticationService.getCurrentUser());
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
        testGroup.setOwner(registeredTestUser1);

        final Group actualGroup = groupService.createGroup(testGroup);

        assertNotNull(actualGroup);

        final Optional<Group> fetchedGroup = groupRepository.findById(actualGroup.getId());

        assertTrue(fetchedGroup.isPresent());
        assertEquals(actualGroup, fetchedGroup.get());
    }

    @Test
    public void addMember_When_CurrentUserOwnerAndGroupExistsAndNewMemberExistsAndNewMemberFriend_Expected_GroupWithNewMember() {
        friendService.createFriendship(registeredTestUser2.getId());

        final Group group = groupService.createGroup(testGroup);

        final Group groupWithMember = groupService.addMember(group.getId(), registeredTestUser2);
        assertNotNull(groupWithMember);
        final Group fetchedGroup = groupService.getGroup(group.getId());

        assertEquals(fetchedGroup, groupWithMember);
    }

    @Test
    public void addMember_When_CurrentUserOwnerAndGroupExistsAndNewMemberExistsAndNewMemberNotFriend_Expected_UserNotFriendException() {
        final Group group = groupService.createGroup(testGroup);

        assertThrows(UserNotFriendException.class,
                     () -> groupService.addMember(group.getId(), registeredTestUser2));
    }

    @Test
    public void deleteMember_When_MemberExist_Expected_DeleteMember() {
        friendService.createFriendship(registeredTestUser2.getId());

        Group group = groupService.createGroup(testGroup);
        group = groupService.addMember(group.getId(), registeredTestUser2);

        assertNotNull(group);

        final Group resultGroup = groupService.deleteMember(group.getId(), registeredTestUser2.getId());

        assertNotNull(resultGroup);
        assertEquals(0, resultGroup.getMembers().size());

        final Optional<Group> fetchedGroup = groupRepository.findById(group.getId());

        assertTrue(fetchedGroup.isPresent());
        assertEquals(fetchedGroup.get(), resultGroup);
    }

}
