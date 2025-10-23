package com.srijan.expence_service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srijan.expence_service.Fegin.GroupInterface;
import com.srijan.expence_service.Fegin.UserGroupInterface;
import com.srijan.expence_service.Fegin.UserInterface;
import com.srijan.expence_service.Model.Dto;
import com.srijan.expence_service.Model.Expence;
import com.srijan.expence_service.Model.ExpenceDTO;
import com.srijan.expence_service.Model.SplitType;
import com.srijan.expence_service.Model.UserExpence;
import com.srijan.expence_service.Repository.ExpenceDTORepository;
import com.srijan.expence_service.Repository.ExpenceRepository;

@Service
public class ExpenceService {
    
    @Autowired
    ExpenceRepository expenceRepository;

    @Autowired
    ExpenceDTORepository expenceDTORepository;

    @Autowired
    GroupInterface groupInterface;
    
    @Autowired
    UserInterface userInterface;

    @Autowired
    UserGroupInterface userGroupInterface;
    
    public Expence addExpence(Expence expence) {
        String userName = userInterface.getUserName(expence.getUserId()).getBody();
        expence.setUserName(userName);
        expenceRepository.save(expence);
        userGroupInterface.updateGroupLogs(expence.getGroupId(),expence.getId());
        ExpenceDTO expenceDTO = new ExpenceDTO();
        expenceDTO.setGroupId(expence.getGroupId());
        expenceDTO.setUserName(expence.getUserName());
        expenceDTO.setSplitType(SplitType.EQUAL);
        expenceDTO.setDescription(expence.getDescription());
        expenceDTO.setAmount(expence.getAmount());

        
        expenceDTORepository.save(expenceDTO);
        return expence;
    }
    
    public void addExpenceEqual(Expence expence, Dto dto) {
        List<Integer> userIdsInDto = dto.getUserIds();
        List<Integer> userIds = groupInterface.getUserIdsInGroup(expence.getGroupId());
        double amount = expence.getAmount();
        ExpenceDTO expenceDTO = new ExpenceDTO();
        expenceDTO.setAmount(amount);
        expenceDTO.setDescription(expence.getDescription());
        expenceDTO.setUserName(expence.getUserName());
        expenceDTO.setSplitType(SplitType.EQUAL);
        expenceDTO.setGroupId(expence.getGroupId());
        List<UserExpence> userExpences = new ArrayList<>();
        for(Integer userId : userIds){
            UserExpence userExpence = new UserExpence();
            double userAmount = amount/userIdsInDto.size();
            String name = userInterface.getUserName(userId).getBody();
            userExpence.setUserName(name);
            if(userIdsInDto.contains(userId)){
                if(name.equals(expence.getUserName())){
                    userExpence.setAmount(amount-userAmount);
                }else{
                    userExpence.setAmount(-1*userAmount);
                }
            }else{
                userExpence.setAmount(0);
            }
            userExpences.add(userExpence);
        }
        expenceDTO.setUserExpences(userExpences);
        expenceRepository.save(expence);
        expenceDTORepository.save(expenceDTO);
        userGroupInterface.updateGroupLogs(expence.getGroupId(),expenceDTO.getId());
    }

    public void addExpencePercentage(Expence expence, Dto dto) {
        List<Integer> userIdsInDto = dto.getUserIds();
        List<Double> percentages = dto.getPercentage();
        if(doubleSum(percentages)!=100.00){
            throw new IllegalArgumentException("Percentage sum must be 100.00");
        }
        List<Integer> userIds = groupInterface.getUserIdsInGroup(expence.getGroupId());
        double amount = expence.getAmount();
        ExpenceDTO expenceDTO = new ExpenceDTO();
        expenceDTO.setAmount(amount);
        expenceDTO.setDescription(expence.getDescription());
        expenceDTO.setUserName(expence.getUserName());
        expenceDTO.setSplitType(SplitType.PERCENTAGE);
        expenceDTO.setGroupId(expence.getGroupId());
        List<UserExpence> userExpences = new ArrayList<>();
        for(Integer userId : userIds){
            UserExpence userExpence = new UserExpence();
            String name = userInterface.getUserName(userId).getBody();
            userExpence.setUserName(name);
            if(userIdsInDto.contains(userId)){
                int index = userIdsInDto.indexOf(userId);
                double userAmount = amount*percentages.get(index)/100.00;
                if(name.equals(expence.getUserName())){
                    userExpence.setAmount(userAmount);
                }else{
                    userExpence.setAmount(-1*userAmount);
                }
            }else{
                userExpence.setAmount(0);
            }
            userExpences.add(userExpence);
        }
        expenceDTO.setUserExpences(userExpences);
        expenceRepository.save(expence);
        expenceDTORepository.save(expenceDTO);
        userGroupInterface.updateGroupLogs(expence.getGroupId(),expenceDTO.getId());
    }

    public void addExpenceCustom(Expence expence, Dto dto) {
        List<Integer> userIdsInDto = dto.getUserIds();
        List<Double> amounts = dto.getAmount();
        if(doubleSum(amounts)!=expence.getAmount()){
            throw new IllegalArgumentException("Amount sum must be equal to expence amount");
        }
        List<Integer> userIds = groupInterface.getUserIdsInGroup(expence.getGroupId());
        ExpenceDTO expenceDTO = new ExpenceDTO();
        expenceDTO.setAmount(expence.getAmount());
        expenceDTO.setDescription(expence.getDescription());
        expenceDTO.setUserName(expence.getUserName());
        expenceDTO.setSplitType(SplitType.CUSTOM);
        expenceDTO.setGroupId(expence.getGroupId());
        List<UserExpence> userExpences = new ArrayList<>();
        for(Integer userId : userIds){
            UserExpence userExpence = new UserExpence();
            String name = userInterface.getUserName(userId).getBody();
            userExpence.setUserName(name);
            if(userIdsInDto.contains(userId)){
                int index = userIdsInDto.indexOf(userId);
                double userAmount = amounts.get(index);
                if(name.equals(expence.getUserName())){
                    userExpence.setAmount(userAmount);
                }else{
                    userExpence.setAmount(-1*userAmount);
                }
            }else{
                userExpence.setAmount(0);
            }
            userExpences.add(userExpence);
        }
        expenceDTO.setUserExpences(userExpences);
        expenceRepository.save(expence);
        expenceDTORepository.save(expenceDTO);
        userGroupInterface.updateGroupLogs(expence.getGroupId(),expenceDTO.getId());
    }

    public List<Expence> addListOfExpence(List<Expence> expences) {
        for(Expence expence : expences){
            addExpence(expence);
        }
        return expences;
    }
    
    // private void MakeExpenceWrapper(Expence expence) {
    //     UserGroupExpenceWrapper userGroupExpenceWrapper=new UserGroupExpenceWrapper();
    //     userGroupExpenceWrapper.setGroupId(expence.getGroupId());
    //     userGroupExpenceWrapper.setUserId(expence.getUserId());
    //     userGroupExpenceWrapper.setUserName(expence.getUserName());
    //     userGroupExpenceWrapper.setAmount(expence.getAmount());
    //     userGroupExpenceWrapper.setDescription(expence.getDescription());

    //     List<ExpenceDTO> expenceDTOs=new ArrayList<>();
    //     for(ExpenceDTO expencedto : expenceDTOs){
    //         Expence expence1 = getExpenceByDescription()
    //         expencedto.setUserId(expence.getUserId());
    //         expencedto.setUserName(expence.getUserName());
    //         expencedto.setAmount(expence.getAmount());
    //         expencedto.setDescription(expence.getDescription());
    //     }

        
    // }
    
    public Expence getExpenceById(int id) {
        return expenceRepository.findById(id).orElse(null);
    }

    public List<Expence> getExpenceByGroupId(int groupId) {
        return expenceRepository.findByGroupId(groupId);
    }
    public List<Expence> getExpenceByDescription(String description) {
        return expenceRepository.findByDescription(description);
    }

    public Expence updateExpenceById(int id, Expence expence) {
        Expence expence1 = expenceRepository.findById(id).orElse(null);
        if(expence1 == null){
            return null;
        }
        expence1.setGroupId(expence.getGroupId());
        expence1.setAmount(expence.getAmount());
        expence1.setUserName(expence.getUserName());
        expence1.setDescription(expence.getDescription());
        expence1.setUserId(expence.getUserId());
        expence1.setSplitType(expence.getSplitType());
        return expenceRepository.save(expence1);
    }

    public void deleteExpence(int id) {
        expenceRepository.deleteById(id);
    }

    public void deleteAllExpenceByGroupId(int groupId) {
        expenceRepository.deleteAllByGroupId(groupId);
    }






    public List<ExpenceDTO> getAllExpenceDTOs(int groupId) {
        // List<ExpenceDTO> expenceDTOs = new ArrayList<>();
        // List<Expence> expences = getExpenceByGroupId(groupId);
        // for(Expence expence : expences){
        //     ExpenceDTO expenceDTO = new ExpenceDTO();
        //     expenceDTO.setGroupId(expence.getGroupId());
        //     expenceDTO.setUserName(expence.getUserName());
        //     expenceDTO.setAmount(expence.getAmount());
        //     expenceDTO.setDescription(expence.getDescription());
        //     expenceDTO.setSplitType(expence.getSplitType());

        //     List<UserExpence> userExpences = new ArrayList<>();

        //     List<String> userNames = groupInterface.getAllUserNameInGroup(groupId);
            
        //     double totalAmount = expenceDTO.getAmount();
        //     double amountPerPerson = totalAmount / userNames.size();
        //     if(expenceDTO.getSplitType() == SplitType.EQUAL){
        //         for(String name : userNames){
        //             UserExpence userExpence = new UserExpence();
        //             userExpence.setUserName(name);
        //             if(name.equals(expenceDTO.getUserName())){
        //                 userExpence.setAmount(totalAmount-amountPerPerson);
        //             }else{
        //                 userExpence.setAmount(-1*amountPerPerson);
        //             }
        //             userExpences.add(userExpence);
        //         }
        //     }
        //     expenceDTO.setUserExpences(userExpences);
        //     expenceDTOs.add(expenceDTO);
        // }
        // return expenceDTOs;
        
        List<ExpenceDTO> expenceDTOs = expenceDTORepository.findAll();
        List<ExpenceDTO> expenceDTOs1 = new ArrayList<>();
        for(ExpenceDTO expenceDTO : expenceDTOs){
            if(expenceDTO.getGroupId() == groupId){
                expenceDTOs1.add(expenceDTO);
            }
        }
        return expenceDTOs1;
    }

    private double doubleSum(List<Double> percentages) {
        double sum = 0;
        for(double percentage : percentages){
            sum += percentage;
        }
        return sum;
    }

    public List<ExpenceDTO> getExpenceDTOByGroupId(int groupId) {
        List<ExpenceDTO> expenceDTOs = expenceDTORepository.findAll();
        List<ExpenceDTO> expenceDTOs1 = new ArrayList<>();
        for(ExpenceDTO expenceDTO : expenceDTOs){
            if(expenceDTO.getGroupId() == groupId){
                expenceDTOs1.add(expenceDTO);
            }
        }
        return expenceDTOs1;
    }

    public ExpenceDTO getExpenceDTOById(int expenceId) {
        return expenceDTORepository.findById(expenceId).orElse(null);
    }





}
