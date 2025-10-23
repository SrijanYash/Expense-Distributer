package com.srijan.group_service.Model;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class GroupAndUserWrapper {

    private Integer id;
    private String groupName;
    @ElementCollection
    @CollectionTable(name = "group_user_details", joinColumns = @JoinColumn(name = "group_id"))
    private List<UserInfoDTO> users;

}
 