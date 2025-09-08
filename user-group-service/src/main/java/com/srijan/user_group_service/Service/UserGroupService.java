package com.srijan.user_group_service.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srijan.user_group_service.FeignClient.ExpenceInterface;
import com.srijan.user_group_service.FeignClient.GroupInterface;
import com.srijan.user_group_service.FeignClient.UserInterface;
import com.srijan.user_group_service.Model.Expence;
import com.srijan.user_group_service.Model.ExpenceDTO;
import com.srijan.user_group_service.Model.Group;
import com.srijan.user_group_service.Model.UserExpence;
import com.srijan.user_group_service.Model.UserGroupView;
import com.srijan.user_group_service.Model.UserInfoDTO;
import com.srijan.user_group_service.Repository.UserGroupViewRepository;

@Service
public class UserGroupService {

    @Autowired
    private UserGroupViewRepository userGroupViewRepository;

    @Autowired
    private UserInterface userInterface;
    
    @Autowired
    private ExpenceInterface expenceInterface;

    @Autowired
    private GroupInterface groupInterface;
    
    public void  createUserGroupView(int groupId, int userId) {
        UserInfoDTO userInfoDTO = userInterface.getUserInfo(userId).getBody();
        if (userInfoDTO == null) {
            throw new IllegalArgumentException("User not found");
        }
        Group group = groupInterface.getGroup(groupId).getBody();
        if (group == null) {
            throw new IllegalArgumentException("Group not found");
        }
        UserGroupView userGroupView = new UserGroupView();
        userGroupView.setGroupId(groupId);
        userGroupView.setGroupName(group.getName());
        userGroupView.setUserId(userId);
        userGroupView.setUserName(userInfoDTO.getName());
        userGroupView.setUserEmail(userInfoDTO.getEmail());
        userGroupView.setUserPhone(userInfoDTO.getPhone());

        List<Integer> userIds = group.getUserIds();
        if(!userIds.contains(userId)) {
            userIds.add(userId);
        }

        List<UserExpence> userExpences = new ArrayList<>();
        for (int id : userIds) {
            String name = userInterface.getUserInfo(id).getBody().getName();
            userExpences.add(new UserExpence( name, 0.0));
        }
        userGroupView.setUserExpenses(userExpences);
        List<ExpenceDTO> expences = expenceInterface.getAllExpenceDTOs(groupId).getBody();
        userGroupView.setGroupLogs(expences);

        userGroupViewRepository.save(userGroupView);
    }

    public void createUserGroupViews(int groupId, List<Integer> userIds) {
        for (int userId : userIds) {
            createUserGroupView(groupId, userId);
        }
    }

    public UserGroupView getUserGroupView(int userId, int groupId) {
        return userGroupViewRepository.findByUserIdAndGroupId(userId, groupId);
    }
    public List<UserGroupView> getGroupUserView(int groupId) {
        return userGroupViewRepository.findByGroupId(groupId);
    }
    
    public List<Expence> getGroupLogs(int groupId) {
        return expenceInterface.getExpenceByGroupId(groupId).getBody();
    }

    public List<Expence> getGroupExpencesByUserId(int groupId, int userId) {
        List<Expence> expences = expenceInterface.getExpenceByGroupId(groupId).getBody();
        List<Expence> userExpences = new ArrayList<>();
        for (Expence expence : expences) {
            if (expence.getUserId() == userId) {
                userExpences.add(expence);
            }
        }
        return userExpences;
    }

    public void updateGroupLogs(int groupId, int expenceDTOId) {
        List<UserGroupView> userGroupViews = getGroupUserView(groupId);
        ExpenceDTO expenceDTO = expenceInterface.getExpenceDTOById(expenceDTOId).getBody();
        
        for (UserGroupView userGroupView : userGroupViews) {
            // Create a new instance of ExpenceDTO for each UserGroupView to avoid concurrent modification issues
            ExpenceDTO newExpenceDTO = new ExpenceDTO();
            newExpenceDTO.setGroupId(expenceDTO.getGroupId());
            newExpenceDTO.setUserName(expenceDTO.getUserName());
            newExpenceDTO.setDescription(expenceDTO.getDescription());
            newExpenceDTO.setAmount(expenceDTO.getAmount());
            newExpenceDTO.setSplitType(expenceDTO.getSplitType());
            newExpenceDTO.setUserExpences(expenceDTO.getUserExpences());
            
            List<ExpenceDTO> expenceDTOs = userGroupView.getGroupLogs();
            expenceDTOs.add(newExpenceDTO);
            userGroupView.setGroupLogs(expenceDTOs);
        }
        
        updateUserExpence(groupId);
        userGroupViewRepository.saveAll(userGroupViews);
    }

    // public void updateUserExpence(int groupId) {
    //     List<UserGroupView> userGroupViews = getGroupUserView(groupId);
    //     for (UserGroupView userGroupView : userGroupViews) {
    //         String name = userGroupView.getUserName();
    //         List<UserExpence> userExpences = userGroupView.getUserExpenses();
    //         List<ExpenceDTO> expenceDTOs = userGroupView.getGroupLogs();
    //         for(UserExpence userExpence : userExpences) {
    //             double amount = 0.0;
    //             for(ExpenceDTO expenceDTO : expenceDTOs) {
    //                 if(expenceDTO.getUserName().equals(name)){
    //                     List<UserExpence> userExpencesDTO = expenceDTO.getUserExpences();
    //                     for(UserExpence userExpenceDTO : userExpencesDTO) {
    //                         if(userExpenceDTO.getUserName().equals(userExpence.getUserName())) {
    //                             amount += userExpenceDTO.getAmount();
    //                         }
    //                     }
    //                 }
    //             }
    //             userExpence.setAmount(amount);
    //         }
    //         for(UserExpence userExpence : userExpences) {
    //             if(userExpence.getUserName().equals(name)) {
    //                 double amount = userExpence.getAmount();
    //                 for(ExpenceDTO expenceDTO : expenceDTOs) {
    //                     if(!expenceDTO.getUserName().equals(name)) {
    //                         List<UserExpence> userExpencesDTO = expenceDTO.getUserExpences();
    //                         for(UserExpence userExpenceDTO : userExpencesDTO) {
    //                             if(userExpenceDTO.getUserName().equals(name)) {
    //                                 amount = userExpenceDTO.getAmount();
    //                             }
    //                         }
    //                     }
    //                 }
    //                 userExpence.setAmount(amount);
    //             }
    //         }
    //         userGroupView.setUserExpenses(userExpences);
    //     }
    // }
    public void updateUserExpence(int groupId) {
        List<UserGroupView> userGroupViews = getGroupUserView(groupId);
        for (UserGroupView userGroupView : userGroupViews) {
            String name = userGroupView.getUserName();
            List<UserExpence> userExpences = userGroupView.getUserExpenses();
            List<ExpenceDTO> expenceDTOs = userGroupView.getGroupLogs();
            for(ExpenceDTO expenceDTO : expenceDTOs) {
                if(!expenceDTO.getDescription().contains("Added")){
                    List<UserExpence> userExpencesDTO = expenceDTO.getUserExpences();
                    String nameDTO = expenceDTO.getUserName();
                    if(expenceDTO.getUserName().equals(name)) {
                        userExpences = updateUserExpence(userExpences,userExpencesDTO);
                        nameDTO = expenceDTO.getUserName();
                    }
                    else{
                        userExpences = updateOneUserExpence(userExpences,userExpencesDTO,name,nameDTO);
                    }
                    String description = expenceDTO.getDescription()+" Added";
                    expenceDTO.setDescription(description);
                }
            }
            userGroupView.setGroupLogs(expenceDTOs);
            userGroupView.setUserExpenses(userExpences);
        }
        userGroupViewRepository.saveAll(userGroupViews);
    }

    public List<UserExpence> updateUserExpence(List<UserExpence> userExpences,List<UserExpence> userExpenceDTOs) {
        for(UserExpence userExpence : userExpences) {
            double amount = userExpence.getAmount();
            for(UserExpence userExpenceDTO : userExpenceDTOs) {
                if(userExpenceDTO.getUserName().equals(userExpence.getUserName())) {
                    amount += userExpenceDTO.getAmount();
                }
            }
            userExpence.setAmount(amount);
        }
        return userExpences;
    }

    public List<UserExpence> updateOneUserExpence(List<UserExpence> userExpences,List<UserExpence> userExpenceDTOs,String name,String nameDTO) {
        double amountDTO = 0.0;
        for(UserExpence userExpenceDTO : userExpenceDTOs) {
            if(userExpenceDTO.getUserName().equals(name)) {
                amountDTO = -userExpenceDTO.getAmount();
            }
        }
        for(UserExpence userExpence : userExpences) {
            if(userExpence.getUserName().equals(nameDTO)) {
                double amount = userExpence.getAmount()+amountDTO;
                userExpence.setAmount(amount);
            }
            if(userExpence.getUserName().equals(name)) {
                double amount = userExpence.getAmount()-amountDTO;
                userExpence.setAmount(amount);
            }
        }
        return userExpences;
    }

    public UserExpence getUserExpenceInUserGroupView(String name, int groupId, int userId) {
        UserGroupView userGroupView = getUserGroupView(userId, groupId);
        List<UserExpence> userExpences = userGroupView.getUserExpenses();
        for(UserExpence userExpence : userExpences) {
            if(userExpence.getUserName().equals(name)) {
                return userExpence;
            }
        }
        return null;
    }

}
