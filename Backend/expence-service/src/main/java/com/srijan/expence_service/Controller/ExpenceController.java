package com.srijan.expence_service.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srijan.expence_service.Fegin.GroupInterface;
import com.srijan.expence_service.Model.Dto;
import com.srijan.expence_service.Model.Expence;
import com.srijan.expence_service.Model.ExpenceDTO;
import com.srijan.expence_service.Model.ExpenceRequest;
// import com.srijan.expence_service.Model.ExpenceDTO;
import com.srijan.expence_service.Model.SplitType;
// import com.srijan.expence_service.Model.UserExpence;
import com.srijan.expence_service.Service.ExpenceService;

@RestController
@RequestMapping("/expence")
public class ExpenceController {

    @Autowired
    ExpenceService expenceService;

    @Autowired
    GroupInterface groupInterface;
    

    @PostMapping("/addExpence/{splitType}")
    public ResponseEntity<Expence> addExpence(@RequestBody ExpenceRequest expenceRequest,@PathVariable int splitType) {
        Expence expence = expenceRequest.getExpence();
        Dto dto = expenceRequest.getSplitDetails();
        List<Integer> userIds = groupInterface.getUserIdsInGroup(expence.getGroupId());
        List<Integer> userIdsInDto = dto.getUserIds();
        if(!userIds.containsAll(userIdsInDto)){
            return ResponseEntity.badRequest().build();
        }
        switch (splitType) {
            case 0 -> {
                expence.setSplitType(SplitType.EQUAL);
                expenceService.addExpence(expence);
            }
            case 1 -> {
                expence.setSplitType(SplitType.EQUAL);
                expenceService.addExpenceEqual(expence,dto);
            }
            case 2 -> {
                expence.setSplitType(SplitType.PERCENTAGE);
                expenceService.addExpencePercentage(expence,dto);
            }
            case 3 -> {
                expence.setSplitType(SplitType.CUSTOM);
                expenceService.addExpenceCustom(expence,dto);
            }
            default -> throw new AssertionError();
        }
        return ResponseEntity.ok(expence);
    }


    @PostMapping("/addListOfExpence")
    public ResponseEntity<List<Expence>> addListOfExpence(@RequestBody List<Expence> expences) {
        List<Expence> expence1 = expenceService.addListOfExpence(expences);
        return ResponseEntity.ok(expence1);
    }

    @GetMapping("/getExpence/{id}")
    public ResponseEntity<Expence> getExpence(@PathVariable int id) {
        Expence expence1 = expenceService.getExpenceById(id);
        if(expence1 == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expence1);
    }
    @GetMapping("/getExpenceByGroupId/{groupId}")
    public ResponseEntity<List<Expence>> getExpenceByGroupId(@PathVariable int groupId) {
        List<Expence> expence1 = expenceService.getExpenceByGroupId(groupId);
        return ResponseEntity.ok(expence1);
    }
    @GetMapping("/getExpenceByDescription/{description}")
    public ResponseEntity<List<Expence>> getExpenceByDescription(@PathVariable String description) {
        List<Expence> expence1 = expenceService.getExpenceByDescription(description);
        if(expence1 == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expence1);
    }
    

    @PutMapping("/updateExpence/{id}")
    public ResponseEntity<Expence> updateExpence(@PathVariable int id, @RequestBody Expence expence) {
        Expence expence1 = expenceService.updateExpenceById(id, expence);
        if(expence1 == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expence1);
    }


    @DeleteMapping("/deleteExpence/{id}")
    public ResponseEntity<String> deleteExpence(@PathVariable int id) {
        Expence expence1 = expenceService.getExpenceById(id);
        if(expence1 == null){
            return ResponseEntity.notFound().build();
        }
        expenceService.deleteExpence(id);
        return ResponseEntity.ok("Expence deleted");
    }
    @DeleteMapping("/deleteAllExpenceByGroupId/{groupId}")
    public ResponseEntity<String> deleteAllExpenceByGroupId(@PathVariable int groupId) {
        expenceService.deleteAllExpenceByGroupId(groupId);
        return ResponseEntity.ok("All Expence in the group are deleted");
    }





    @GetMapping("/getAllExpenceDTOs/{groupId}")
    public ResponseEntity<List<ExpenceDTO>> getAllExpenceDTOs(@PathVariable int groupId) {
        List<ExpenceDTO> expenceDTOs = expenceService.getAllExpenceDTOs(groupId);
        return ResponseEntity.ok(expenceDTOs);
    }
    @GetMapping("/getExpenceDTO/{expenceId}")
    public ResponseEntity<ExpenceDTO> getExpenceDTOById(@PathVariable int expenceId) {
        ExpenceDTO expenceDTO = expenceService.getExpenceDTOById(expenceId);
        if(expenceDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expenceDTO);
    }

    @GetMapping("/getExpenceDTOFromGroup/{groupId}")
    public ResponseEntity<List<ExpenceDTO>> getExpenceDTO(@PathVariable int groupId) {
        List<ExpenceDTO> expenceDTOs = expenceService.getExpenceDTOByGroupId(groupId);
        if(expenceDTOs == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expenceDTOs);
    }
}
