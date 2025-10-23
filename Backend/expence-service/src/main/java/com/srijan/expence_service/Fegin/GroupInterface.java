package com.srijan.expence_service.Fegin;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "GROUP-SERVICE")
public interface GroupInterface {
    
    @GetMapping("/group/{groupId}/ListOfUsers")
    public List<Integer> getUserIdsInGroup(@PathVariable("groupId") Integer givenGroupId);

    @GetMapping("/group/{groupId}/GetAllUserNameInGroup")
    public List<String> getAllUserNameInGroup(@PathVariable("groupId") Integer givenGroupId);

}
