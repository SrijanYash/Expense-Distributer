package com.srijan.group_service.Fegin;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

// import com.srijan.group_service.Model.Group;

@FeignClient(name="USER-GROUP-SERVICE")
public interface UserGroupInterface {

    @PostMapping("/user-group/{userId}/NewUserGroupView/{groupId}")
    public String createUserGroupView(@PathVariable int userId, @PathVariable int groupId);

    @PostMapping("/user-group/{groupId}/NewUserGroupViews")
    public String createUserGroupViews(@PathVariable int groupId, @RequestBody List<Integer> userIds);
    
}
