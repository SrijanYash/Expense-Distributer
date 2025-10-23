package com.srijan.user_group_service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.srijan.user_group_service.Model.UserInfoDTO;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @GetMapping("/user/getUserName/{id}")
    public ResponseEntity<String> getUserName(@PathVariable int id);

    @GetMapping("/user/UserDetails/{id}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable int id);

}
