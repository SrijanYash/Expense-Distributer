package com.srijan.user_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srijan.user_service.Model.User;
import com.srijan.user_service.Model.UserInfoDTO;
import com.srijan.user_service.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    //register one user
    public User registerUser(User user) {
        return userRepository.save(user);
    }
    
    //register multiple users
    public List<User> registerUsers(List<User> users) {
        return userRepository.saveAll(users);
    }
    
    //get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    //get user by name
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }
    //get user by id
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }
    public String getUserName(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return user.getName();
        }
        return null;
    }
    

    //update user by name
    public User updateUser(String name, User user) {
        User existingUser = userRepository.findByName(name).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhone(user.getPhone());
            return userRepository.save(existingUser);
        }
        return null;
    }
    //update user by id
    public User updateUserById(int id, User user) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            existingUser.setPassword(user.getPassword());
            existingUser.setPhone(user.getPhone());
            return userRepository.save(existingUser);
        }
        return null;
    }


    //delete user by name
    public void deleteUser(String name) {
        userRepository.deleteById(getUserByName(name).getId());
    }
    //delete user by id
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }


    //get user info by id
    public UserInfoDTO getUserInfoById(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            return new UserInfoDTO(user.getName(), user.getPhone(), user.getEmail());
        }
        return null;
    }




}
