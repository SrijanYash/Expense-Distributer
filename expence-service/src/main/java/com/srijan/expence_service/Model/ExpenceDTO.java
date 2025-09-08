package com.srijan.expence_service.Model;

import java.util.List;

import jakarta.persistence.CollectionTable;
// import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity(name = "expence_dto")
public class ExpenceDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int groupId;
    private String userName;
    private String description;
    private double amount;
    
    @Enumerated(EnumType.STRING)
    private SplitType splitType;

    @ElementCollection
    @CollectionTable(name = "expence_user_expence", joinColumns = @JoinColumn(name = "expence_dto_id"))
    private List<UserExpence> userExpences;

}
