package com.srijan.user_service.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.srijan.user_service.Model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);

    @Query("select u from User u where lower(trim(u.email)) = lower(trim(:email))")
    Optional<User> findByEmailIgnoreCase(@Param("email") String email);


    @Query("select u from User u join com.srijan.user_service.Model.Friend f on u.id = f.friendId where f.userId = :userId")
    List<User> findFriendsForUser(@Param("userId") Integer userId);

}
