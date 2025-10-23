package com.srijan.expence_service.Fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-GROUP-SERVICE")
public interface UserGroupInterface {

    @GetMapping("/user-group/{groupId}/GroupExpenceDTOUpdate/{expenceDTOId}")
    public void updateGroupLogs(@PathVariable int groupId, @PathVariable int expenceDTOId);

}
