package com.srijan.user_service.Controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srijan.user_service.Model.User;
import com.srijan.user_service.Model.UserInfoDTO;
import com.srijan.user_service.Service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    //create user
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            return ResponseEntity.ok(registeredUser);
        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/registerUsers")
    public ResponseEntity<List<User>> registerUsers(@RequestBody List<User> users) {
        List<User> registeredUsers = userService.registerUsers(users);
        if (registeredUsers != null) {
            return ResponseEntity.ok(registeredUsers);
        }
        return ResponseEntity.notFound().build();
    }



    //get all users
    @GetMapping("/allUsers")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getAllUsersFallback")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users != null) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<List<User>> getAllUsersFallback(Throwable throwable) {
        System.out.println("Fallback method called due to: " + throwable.getMessage());
        return ResponseEntity.ok(Collections.emptyList());
    }

    //get user by id
    @GetMapping("/getUserByName/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        User user = userService.getUserByName(name);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    //get user by id
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    //gets user name by id
    @GetMapping("/getUserName/{id}")
    public ResponseEntity<String> getUserName(@PathVariable int id) {
        String name = userService.getUserName(id);
        if (name != null) {
            return ResponseEntity.ok(name);
        }
        return ResponseEntity.notFound().build();
    }


    //update user
    @PutMapping("/updateUser/{name}")
    public ResponseEntity<User> updateUser(@PathVariable String name, @RequestBody User user) {
        User updatedUser = userService.updateUser(name, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    //update user by id
    @PutMapping("/updateUserById/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable int id, @RequestBody User user) {
        User updatedUser = userService.updateUserById(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }


    
    //delete user by name
    @DeleteMapping("/deleteUser/{name}")
    public ResponseEntity<String> deleteUser(@PathVariable String name) {
        User user = userService.getUserByName(name);
        if (user != null) {
            userService.deleteUser(name);
            return ResponseEntity.ok("User "+name+" deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }
    //delete user by id
    @DeleteMapping("/deleteUserById/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User "+id+" deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/UserDetails/{id}")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable int id) {
        UserInfoDTO userInfoDTO = userService.getUserInfoById(id);
        if (userInfoDTO != null) {
            return ResponseEntity.ok(userInfoDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
