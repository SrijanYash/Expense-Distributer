package com.srijan.group_service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.srijan.group_service.Fegin.UserGroupInterface;
import com.srijan.group_service.Fegin.UserInterface;
import com.srijan.group_service.Model.Group;
import com.srijan.group_service.Model.GroupAndUserWrapper;
import com.srijan.group_service.Model.UserInfoDTO;
import com.srijan.group_service.Repository.GroupRepository;

@Service
public class GroupService {
    
    @Autowired
    private GroupRepository groupRepository;
    
    @Autowired
    private UserInterface userInterface;

    @Autowired
    private UserGroupInterface userGroupInterface;
    
    // Create a new group
    public Group createGroup(Group group) {
        Group group1 =groupRepository.save(group);
        List<Integer> userIds = group1.getUserIds();
        userGroupInterface.createUserGroupViews(group1.getGroupId(), userIds);
        return group1;
    }
    public String createGroupByUser(int userId, int groupId) {
        userGroupInterface.createUserGroupView(userId, groupId);
        return "Group created successfully";
    }
    // public void updateUserView(int userId, int groupId) {
    //     userGroupInterface.createUserGroupView(userId, groupId);
    // }
    
    // Add users to a group
    public Group addUsersToGroup(Integer givenGroupId, List<Integer> givenUserIds) {
        Group group = groupRepository.findById(givenGroupId).orElseThrow(() -> new RuntimeException("Group not found"));
        group.getUserIds().addAll(givenUserIds);
        Group group1 = groupRepository.save(group);
        userGroupInterface.createUserGroupViews(givenGroupId, givenUserIds);
        return group1;
    }

    // Remove a user from a group
    public Group deleteUserFromGroup(Integer givenGroupId, Integer givenUserId) {
        Group group = groupRepository.findById(givenGroupId).orElseThrow(() -> new RuntimeException("Group not found"));
        if(group.getUserIds().contains(givenUserId)){
            group.getUserIds().remove(givenUserId);
        }else{
            throw new RuntimeException("User not found in the group");
        }
        return groupRepository.save(group);
    }

    // Get a group by ID
    public Group getGroup(Integer givenGroupId) {
        return groupRepository.findById(givenGroupId).orElseThrow(() -> new RuntimeException("Group not found"));
    }
    
    public List<Integer> getUserIdsInGroup(Integer givenGroupId) {
        return groupRepository.findById(givenGroupId).orElseThrow(() -> new RuntimeException("Group not found")).getUserIds();
    }
    public List<String> getAllUserNameInGroup(Integer givenGroupId) {
        GroupAndUserWrapper groupAndUserWrappers = getUsersInGroupWrapper(givenGroupId);
        List<String> userNames = new ArrayList<>();
        for (UserInfoDTO user : groupAndUserWrappers.getUsers()) {
            userNames.add(user.getName());
        }
        return userNames;
    }

    // Get all users in a group
    public GroupAndUserWrapper getUsersInGroupWrapper(Integer givenGroupId) {
        Group group = groupRepository.findById(givenGroupId).orElseThrow(() -> new RuntimeException("Group not found"));
        GroupAndUserWrapper groupAndUserWrapper = new GroupAndUserWrapper();
        groupAndUserWrapper.setId(group.getGroupId());
        groupAndUserWrapper.setGroupName(group.getName());
        List<Integer> userIds = group.getUserIds();
        List<UserInfoDTO> userInfoDTOs = new ArrayList<>();
        for (Integer userId : userIds) {
            ResponseEntity<UserInfoDTO> userInfoDTO = userInterface.getUserInfo(userId);
            userInfoDTOs.add(userInfoDTO.getBody());
        }
        groupAndUserWrapper.setUsers(userInfoDTOs);

        return groupAndUserWrapper;
    }



}
