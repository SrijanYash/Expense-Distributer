package com.srijan.user_group_service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srijan.user_group_service.Model.Group;

@FeignClient(name = "GROUP-SERVICE")
public interface GroupInterface {

    @GetMapping("/group/{groupId}/GetGroup")
    public ResponseEntity<Group> getGroup(@PathVariable("groupId") int givenGroupId);

}
