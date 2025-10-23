package com.srijan.user_group_service.Model;

// ExpenceDTO.java - Now as an Entity
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Data;

@Data
@Entity
@Table(name = "gv-expense_dto")
public class ExpenceDTO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Version
    private Long version;
    
    private int groupId;
    private String userName;
    private String description;
    private double amount;
    
    @Enumerated(EnumType.STRING)
    private SplitType splitType;
    
    @ElementCollection
    @CollectionTable(name = "gv-expense_user_expenses", 
        joinColumns = @JoinColumn(name = "gv-expense_dto_id"))
    private List<UserExpence> userExpences;
}