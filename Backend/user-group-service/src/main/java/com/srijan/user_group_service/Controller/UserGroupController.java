package com.srijan.user_group_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srijan.user_group_service.Model.Expence;
import com.srijan.user_group_service.Model.UserGroupView;
import com.srijan.user_group_service.Service.UserGroupService;

@RestController
@RequestMapping("/user-group")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    //create a user group view
    @PostMapping("/{userId}/NewUserGroupView/{groupId}")
    public void createUserGroupView(@PathVariable int userId, @PathVariable int groupId) {
        userGroupService.createUserGroupView(userId, groupId);
    }
    @PostMapping("/{groupId}/NewUserGroupViews")
    public void createUserGroupViews(@PathVariable int groupId, @RequestBody List<Integer> userIds) {
        userGroupService.createUserGroupViews(groupId, userIds);
    }

    //get a user group view
    @GetMapping("/userId-{userId}/groupId-{groupId}")
    public UserGroupView getUserGroupView(@PathVariable int userId, @PathVariable int groupId) {
        return userGroupService.getUserGroupView(userId, groupId);
    }
    @GetMapping("/groupId-{groupId}/getGroupUserView")
    public List<UserGroupView> getGroupUserView(@PathVariable int groupId) {
        return userGroupService.getGroupUserView(groupId);
    }

    //get group logs
    @GetMapping("/groupId-{groupId}/GroupLogs")
    public List<Expence> getGroupLogs(@PathVariable int groupId) {
        return userGroupService.getGroupLogs(groupId);
    }

    //get group expences by user id
    @GetMapping("/groupId-{groupId}/GroupExpences/usersView/{userId}")
    public List<Expence> getGroupExpencesByUserId(@PathVariable int groupId, @PathVariable int userId) {
        return userGroupService.getGroupExpencesByUserId(groupId, userId);
    }

    @GetMapping("/{groupId}/GroupExpenceDTOUpdate/{expenceDTOId}")
    public void updateGroupLogs(@PathVariable int groupId, @PathVariable int expenceDTOId) {
        userGroupService.updateGroupLogs(groupId, expenceDTOId);
    }

    @GetMapping("/groupId-{groupId}/UpdateUserExpence")
    public void updateUserExpence(@PathVariable int groupId) {
        userGroupService.updateUserExpence(groupId);
    }

    // @GetMapping("/groupId-{groupId}/getGroupExpenceDTO")
    // public ExpenceDTO getGroupExpenceDTO(@PathVariable int groupId) {
    //     return userGroupService.getGroupExpenceDTO(groupId);
    // }

}
