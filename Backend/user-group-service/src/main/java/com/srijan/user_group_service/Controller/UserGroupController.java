package com.srijan.user_group_service.Controller;

import java.util.ArrayList;
import java.util.Collections;
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

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/user-group")
public class UserGroupController {

    @Autowired
    private UserGroupService userGroupService;

    //create a user group view
    @PostMapping("/{userId}/NewUserGroupView/{groupId}")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "createUserGroupViewFallback")
    public void createUserGroupView(@PathVariable int userId, @PathVariable int groupId) {
        userGroupService.createUserGroupView(groupId, userId);
    }
    public void createUserGroupViewFallback(int userId, int groupId, Throwable throwable) {
        System.out.println("Fallback called for createUserGroupView due to: " + throwable.getMessage());
    }

    @PostMapping("/{groupId}/NewUserGroupViews")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "createUserGroupViewsFallback")
    public void createUserGroupViews(@PathVariable int groupId, @RequestBody List<Integer> userIds) {
        userGroupService.createUserGroupViews(groupId, userIds);
    }
    public void createUserGroupViewsFallback(int groupId, List<Integer> userIds, Throwable throwable) {
        System.out.println("Fallback called for createUserGroupViews due to: " + throwable.getMessage());
    }

    //get a user group view
    @GetMapping("/userId-{userId}/groupId-{groupId}")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getUserGroupViewFallback")
    public UserGroupView getUserGroupView(@PathVariable int userId, @PathVariable int groupId) {
        return userGroupService.getUserGroupView(userId, groupId);
    }
    public UserGroupView getUserGroupViewFallback(int userId, int groupId, Throwable throwable) {
        return new UserGroupView();
    }

    @GetMapping("/groupId-{groupId}/getGroupUserView")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupUserViewFallback")
    public List<UserGroupView> getGroupUserView(@PathVariable int groupId) {
        return userGroupService.getGroupUserView(groupId);
    }
    @GetMapping("/{groupId}/getGroupUserView")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupUserViewFallback")
    public List<UserGroupView> getGroupUserViewAlt(@PathVariable int groupId) {
        return userGroupService.getGroupUserView(groupId);
    }
    public List<UserGroupView> getGroupUserViewFallback(int groupId, Throwable throwable) {
        return Collections.emptyList();
    }

    //get group logs
    @GetMapping("/groupId-{groupId}/GroupLogs")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupLogsFallback")
    public List<Expence> getGroupLogs(@PathVariable int groupId) {
        return userGroupService.getGroupLogs(groupId);
    }
    @GetMapping("/{groupId}/GroupLogs")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupLogsFallback")
    public List<Expence> getGroupLogsAlt(@PathVariable int groupId) {
        return userGroupService.getGroupLogs(groupId);
    }

    @GetMapping("/groupId-{groupId}/MemberNames")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupMemberNamesFallback")
    public List<String> getGroupMemberNames(@PathVariable int groupId) {
        return userGroupService.getGroupMemberNames(groupId);
    }
    @GetMapping("/{groupId}/MemberNames")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupMemberNamesFallback")
    public List<String> getGroupMemberNamesAlt(@PathVariable int groupId) {
        return userGroupService.getGroupMemberNames(groupId);
    }
    public List<String> getGroupMemberNamesFallback(int groupId, Throwable throwable) {
        return new ArrayList<>();
    }

    @GetMapping("/groupId-{groupId}/MemberDetails")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupMemberDetailsFallback")
    public List<com.srijan.user_group_service.Model.MemberDTO> getGroupMemberDetails(@PathVariable int groupId) {
        return userGroupService.getGroupMemberDetails(groupId);
    }
    @GetMapping("/{groupId}/MemberDetails")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupMemberDetailsFallback")
    public List<com.srijan.user_group_service.Model.MemberDTO> getGroupMemberDetailsAlt(@PathVariable int groupId) {
        return userGroupService.getGroupMemberDetails(groupId);
    }
    public List<com.srijan.user_group_service.Model.MemberDTO> getGroupMemberDetailsFallback(int groupId, Throwable throwable) {
        return new ArrayList<>();
    }
    public List<Expence> getGroupLogsFallback(int groupId, Throwable throwable) {
        return Collections.emptyList();
    }

    //get group expences by user id
    @GetMapping("/groupId-{groupId}/GroupExpences/usersView/{userId}")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "getGroupExpencesByUserIdFallback")
    public List<Expence> getGroupExpencesByUserId(@PathVariable int groupId, @PathVariable int userId) {
        return userGroupService.getGroupExpencesByUserId(groupId, userId);
    }
    public List<Expence> getGroupExpencesByUserIdFallback(int groupId, int userId, Throwable throwable) {
        return Collections.emptyList();
    }

    @GetMapping("/{groupId}/GroupExpenceDTOUpdate/{expenceDTOId}")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "updateGroupLogsFallback")
    public void updateGroupLogs(@PathVariable int groupId, @PathVariable int expenceDTOId) {
        userGroupService.updateGroupLogs(groupId, expenceDTOId);
    }
    public void updateGroupLogsFallback(int groupId, int expenceDTOId, Throwable throwable) {
        System.out.println("Fallback called for updateGroupLogs due to: " + throwable.getMessage());
    }

    @GetMapping("/groupId-{groupId}/UpdateUserExpence")
    @CircuitBreaker(name = "userGroupBreaker", fallbackMethod = "updateUserExpenceFallback")
    public void updateUserExpence(@PathVariable int groupId) {
        userGroupService.updateUserExpence(groupId);
    }
    public void updateUserExpenceFallback(int groupId, Throwable throwable) {
        System.out.println("Fallback called for updateUserExpence due to: " + throwable.getMessage());
    }

    // @GetMapping("/groupId-{groupId}/getGroupExpenceDTO")
    // public ExpenceDTO getGroupExpenceDTO(@PathVariable int groupId) {
    //     return userGroupService.getGroupExpenceDTO(groupId);
    // }

}
