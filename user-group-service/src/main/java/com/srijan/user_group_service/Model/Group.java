package com.srijan.user_group_service.Model;
import java.util.List;

import lombok.Data;

@Data
public class Group {

    private Integer groupId;
    private String name;
    private List<Integer> userIds;
}
