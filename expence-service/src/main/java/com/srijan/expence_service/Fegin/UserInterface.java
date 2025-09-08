package com.srijan.expence_service.Fegin;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "USER-SERVICE")
public interface UserInterface {

    @GetMapping("/user/getUserName/{id}")
    public ResponseEntity<String> getUserName(@PathVariable int id);

}
