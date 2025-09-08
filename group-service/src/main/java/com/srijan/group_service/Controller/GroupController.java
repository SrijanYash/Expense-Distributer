package com.srijan.group_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srijan.group_service.Model.Group;
import com.srijan.group_service.Model.GroupAndUserWrapper;
import com.srijan.group_service.Service.GroupService;

@RestController
@RequestMapping("/group")
public class GroupController {

    @Autowired
    private GroupService groupService;

    // Create a new group
    @PostMapping("/createGroup")
    public ResponseEntity<Group> createGroup(@RequestBody Group group) {
        return ResponseEntity.ok(groupService.createGroup(group));
    }
    @PostMapping("/{userId}/UserGroupView/{groupId}")
    public ResponseEntity<String> createUserGroupView(@PathVariable int userId, @PathVariable int groupId) {
        return ResponseEntity.ok(groupService.createGroupByUser(userId,groupId));
    }

    // Add users to a group
    @PostMapping("/{groupId}/AddUsers")
    public ResponseEntity<Group> addUsersToGroup(@PathVariable("groupId") Integer givenGroupId, @RequestBody List<Integer> givenUserIds){
        return ResponseEntity.ok(groupService.addUsersToGroup(givenGroupId, givenUserIds));
    }

    // Remove a user from a group
    @DeleteMapping("/{groupId}/DeleteUser/{userId}")
    public ResponseEntity<Group> deleteUserFromGroup(@PathVariable("groupId") Integer givenGroupId, @PathVariable("userId") Integer givenUserId){
        return ResponseEntity.ok(groupService.deleteUserFromGroup(givenGroupId, givenUserId));
    }

    // Get a group by ID
    @GetMapping("/{groupId}/GetGroup")
    public ResponseEntity<Group> getGroup(@PathVariable("groupId") Integer givenGroupId){
        return ResponseEntity.ok(groupService.getGroup(givenGroupId));
    }
    @GetMapping("/{groupId}/ListOfUsers")
    public List<Integer> getUserIdsInGroup(@PathVariable("groupId") Integer givenGroupId){
        return groupService.getUserIdsInGroup(givenGroupId);
    }
    @GetMapping("/{groupId}/GetAllUserNameInGroup")
    public List<String> getAllUserNameInGroup(@PathVariable("groupId") Integer givenGroupId){
        return groupService.getAllUserNameInGroup(givenGroupId);
    }

    // Get all users in a group
    @GetMapping("/{groupId}/GroupWrapper")
    public ResponseEntity<GroupAndUserWrapper> getUsersInGroupWrapper(@PathVariable("groupId") Integer givenGroupId){
        return ResponseEntity.ok(groupService.getUsersInGroupWrapper(givenGroupId));
    }

}
