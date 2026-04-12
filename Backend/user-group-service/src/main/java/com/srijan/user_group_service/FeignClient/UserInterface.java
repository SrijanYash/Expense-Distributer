package com.srijan.user_group_service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.srijan.user_group_service.Model.UserInfoDTO;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @GetMapping("/user/getUserName/{id}")
    public ResponseEntity<String> getUserName(@PathVariable int id);

    @GetMapping("/user/UserDetails/{id}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable int id);

    @PostMapping("/user/getUsersInfoByBatch")
    public ResponseEntity<List<UserInfoDTO>> getUsersInfoByBatch(@RequestBody List<Integer> userIds);

}
