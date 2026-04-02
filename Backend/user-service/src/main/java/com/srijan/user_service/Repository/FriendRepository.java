package com.srijan.user_service.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.srijan.user_service.Model.Friend;

public interface FriendRepository extends JpaRepository<Friend, Integer> {
    List<Friend> findByUserId(Integer userId);
    boolean existsByUserIdAndFriendId(Integer userId, Integer friendId);
    void deleteByUserIdAndFriendId(Integer userId, Integer friendId);
}