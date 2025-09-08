package com.srijan.expence_service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;

@Data
@Entity
@Getter
@Table(name = "expence")
public class Expence {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expence_id")
    private int id;
    
    private int groupId;
    private int userId;
    private String userName;
    private double amount;
    private String description;
    private SplitType splitType;

}
