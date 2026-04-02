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
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping("/login")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "loginFallback")
    public ResponseEntity<String> login(@RequestBody User user) {
        boolean authenticated = userService.authenticate(user);
        if (authenticated) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid user");
    }
    public ResponseEntity<String> loginFallback(Throwable throwable) {
        System.out.println("Fallback method called due to: " + throwable.getMessage());
        return ResponseEntity.status(503).body("Login service unavailable");
    }
    //create user
    @PostMapping("/register")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "registerUserFallback")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(user);
        if (registeredUser != null) {
            return ResponseEntity.ok("Welcome to Expense Distributer " + registeredUser.getName());
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<String> registerUserFallback(Throwable throwable) {
        System.out.println("Fallback method called due to: " + throwable.getMessage());
        return ResponseEntity.ok("User registration failed " + throwable.getMessage());
    }
    @PostMapping("/registerUsers")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "registerUserFallback")
    public ResponseEntity<String> registerUsers(@RequestBody List<User> users) {
        List<User> registeredUsers = userService.registerUsers(users);
        if (registeredUsers != null) {
            return ResponseEntity.ok("Users registered successfully");
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
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByNameFallback")
    public ResponseEntity<User> getUserByName(@PathVariable String name) {
        User user = userService.getUserByName(name);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<String> getUserByNameFallback(String name, Throwable throwable) {
        System.out.println("Fallback method called due to: " + throwable.getMessage());
        return ResponseEntity.ok("No user found with name: " + name);
    }

    //get user by id
    @GetMapping("/getUserById/{id}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    //gets user name by id
    @GetMapping("/getUserName/{id}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<String> getUserName(@PathVariable int id) {
        String name = userService.getUserName(id);
        if (name != null) {
            return ResponseEntity.ok(name);
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<String> getUserByIdFallback(int id, Throwable throwable) {
        System.out.println("Fallback method called due to: " + throwable.getMessage());
        return ResponseEntity.ok("No user found with id: " + id);
    }


    //update user
    @PutMapping("/updateUser/{name}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByNameFallback")
    public ResponseEntity<User> updateUser(@PathVariable String name, @RequestBody User user) {
        User updatedUser = userService.updateUser(name, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }
    //update user by id
    @PutMapping("/updateUserById/{id}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<User> updateUserById(@PathVariable int id, @RequestBody User user) {
        User updatedUser = userService.updateUserById(id, user);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.notFound().build();
    }


    
    //delete user by name
    @DeleteMapping("/deleteUser/{name}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByNameFallback")
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
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<String> deleteUserById(@PathVariable int id) {
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUserById(id);
            return ResponseEntity.ok("User "+id+" deleted successfully");
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/UserDetails/{id}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserInfoFallback")
    public ResponseEntity<UserInfoDTO> getUserInfo(@PathVariable int id) {
        UserInfoDTO userInfoDTO = userService.getUserInfoById(id);
        if (userInfoDTO != null) {
            return ResponseEntity.ok(userInfoDTO);
        }
        return ResponseEntity.notFound().build();
    }
    public ResponseEntity<UserInfoDTO> getUserInfoFallback(int id, Throwable throwable) {
        return ResponseEntity.ok(new UserInfoDTO());
    }

    @GetMapping("/{userId}/friends")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getAllUsersFallback")
    public ResponseEntity<List<User>> getFriends(@PathVariable int userId) {
        List<User> users = userService.getFriends(userId);
        if (users != null) {
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{userId}/friends")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "registerUserFallback")
    public ResponseEntity<?> addFriend(@PathVariable int userId, @RequestBody User payload) {
        try {
            User friend = userService.addFriend(userId, payload.getName(), payload.getEmail());
            return ResponseEntity.ok(friend);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "getUserByIdFallback")
    public ResponseEntity<String> removeFriend(@PathVariable int userId, @PathVariable int friendId) {
        userService.removeFriend(userId, friendId);
        return ResponseEntity.ok("Friend removed");
    }

    @PostMapping("/{userId}/friends/invite")
    @CircuitBreaker(name = "userBreaker", fallbackMethod = "registerUserFallback")
    public ResponseEntity<?> inviteFriend(@PathVariable int userId, @RequestBody User payload) {
        try {
            User friend = userService.inviteFriend(userId, payload.getName(), payload.getEmail());
            return ResponseEntity.ok(friend);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/confirm")
    public ResponseEntity<String> confirm(@RequestParam("token") String token) {
        String result = userService.confirmRegistration(token);
        return ResponseEntity.ok(result);
    }
}
