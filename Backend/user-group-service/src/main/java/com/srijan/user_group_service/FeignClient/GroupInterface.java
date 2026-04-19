package com.srijan.user_group_service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srijan.user_group_service.Model.Group;

@FeignClient(name = "GROUP-SERVICE", url = "${GROUP_SERVICE_URL:http://localhost:8082}")
public interface GroupInterface {

    @GetMapping("/group/{groupId}/GetGroup")
    public ResponseEntity<Group> getGroup(@PathVariable("groupId") int givenGroupId);

    @GetMapping("/group/{groupId}/GetAllUserNameInGroup")
    public ResponseEntity<List<String>> getAllUserNameInGroup(@PathVariable("groupId") int givenGroupId);

}
