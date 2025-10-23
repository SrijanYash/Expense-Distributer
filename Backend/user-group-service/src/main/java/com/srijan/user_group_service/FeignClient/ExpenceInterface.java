package com.srijan.user_group_service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.srijan.user_group_service.Model.Expence;
import com.srijan.user_group_service.Model.ExpenceDTO;

@FeignClient(name = "EXPENCE-SERVICE")
public interface ExpenceInterface {

    @GetMapping("/expence/getExpenceByGroupId")
    public ResponseEntity<List<Expence>> getExpenceByGroupId(@RequestParam int groupId);

    @GetMapping("/expence/getAllExpenceDTOs/{groupId}")
    public ResponseEntity<List<ExpenceDTO>> getAllExpenceDTOs(@PathVariable int groupId);

    @GetMapping("/expence/getExpenceDTO/{expenceId}")
    public ResponseEntity<ExpenceDTO> getExpenceDTOById(@PathVariable int expenceId);

}
