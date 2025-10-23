package com.srijan.group_service.Fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srijan.group_service.Model.UserInfoDTO;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @GetMapping("/user/UserDetails/{id}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable Integer id);

}
