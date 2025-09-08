package com.srijan.group_service.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupAndUserWrapper {

    private Integer id;
    private String groupName;
    private List<UserInfoDTO> users;

}
 