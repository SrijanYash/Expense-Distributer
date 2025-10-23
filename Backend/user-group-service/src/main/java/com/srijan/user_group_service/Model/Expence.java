package com.srijan.user_group_service.Model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Expence {

    private int expenceId;
    private int groupId;
    private int userId;
    private String userName;
    private double amount;
    private String description;
    private SplitType splitType;
}
