package com.srijan.user_service.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_friends", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","friend_id"}))
@Data
@NoArgsConstructor
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "friend_id", nullable = false)
    private Integer friendId;

    public Friend(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
}