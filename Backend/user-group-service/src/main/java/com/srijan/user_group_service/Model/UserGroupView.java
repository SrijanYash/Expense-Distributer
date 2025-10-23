package com.srijan.user_group_service.Model;

// UserGroupView.java
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_group_view")
public class UserGroupView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private int userId;
    private int groupId;
    private String groupName;
    private String userName;
    private String userEmail;
    private long userPhone;

    @ElementCollection
    @CollectionTable(name = "gv-user_expenses", 
        joinColumns = @JoinColumn(name = "user_group_view_id"))
    private List<UserExpence> userExpenses;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_group_view_id")
    private List<ExpenceDTO> groupLogs;
}
