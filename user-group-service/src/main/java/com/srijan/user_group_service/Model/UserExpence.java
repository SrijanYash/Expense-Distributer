package com.srijan.user_group_service.Model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// UserExpence.java - Remains as Embeddable

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class UserExpence {
    private String userName;
    private double amount;
}
