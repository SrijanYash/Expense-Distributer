package com.srijan.user_service.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srijan.user_service.Model.Friend;
import com.srijan.user_service.Model.Invitation;
import com.srijan.user_service.Model.User;
import com.srijan.user_service.Model.UserInfoDTO;
import com.srijan.user_service.Repository.FriendRepository;
import com.srijan.user_service.Repository.InvitationRepository;
import com.srijan.user_service.Repository.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private InvitationRepository invitationRepository;

    @Autowired
    private org.springframework.beans.factory.ObjectProvider<org.springframework.mail.javamail.JavaMailSender> mailSenderProvider;
    
    //register one user
    public User registerUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("Invalid user");
        }
        if (user.getName() != null && userRepository.findByName(user.getName()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        if (user.getEmail() != null && userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword("123456");
        }
        return userRepository.save(user);
    }
    
    //register multiple users
    public List<User> registerUsers(List<User> users) {
        for(User user : users) {
            if(userRepository.findByName(user.getName()).isPresent()) {
                throw new IllegalArgumentException("User already exists");
            }
        }
        return userRepository.saveAll(users);
    }
    
    //get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    //get user by name
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    //get user by id
    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
    public String getUserName(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user != null) {
            return user.getName();
        }
        return null;
    }
    

    //update user by name
    public User updateUser(String name, User user) {
        User existingUser = userRepository.findByName(name).orElseThrow(() -> new IllegalArgumentException("User not found"));
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
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user != null) {
            return new UserInfoDTO(user.getId(), user.getName(), user.getPhone(), user.getEmail());
        }
        return null;
    }

    //get user info by ids (batch)
    public List<UserInfoDTO> getUsersInfoByIds(List<Integer> userIds) {
        return userRepository.findAllById(userIds).stream()
            .map(user -> new UserInfoDTO(user.getId(), user.getName(), user.getPhone(), user.getEmail()))
            .toList();
    }

    //authenticate login by name or email and password
    public boolean authenticate(User loginUser) {
        if (loginUser == null) {
            return false;
        }
        String password = loginUser.getPassword();
        if (password == null) {
            return false;
        }
        String email = loginUser.getEmail();
        if (email != null && !email.isEmpty()) {
            return userRepository.findByEmail(email)
                    .map(u -> password.equals(u.getPassword()))
                    .orElse(false);
        }
        String name = loginUser.getName();
        if (name != null && !name.isEmpty()) {
            return userRepository.findByName(name)
                    .map(u -> password.equals(u.getPassword()))
                    .orElse(false);
        }
        return false;
    }

    public User addFriend(int userId, String name, String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email required");
        }
        String emailTrimmed = email.trim();
        if (emailTrimmed.isEmpty()) {
            throw new IllegalArgumentException("Email required");
        }
        User friendUser = userRepository.findByEmailIgnoreCase(emailTrimmed)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (name != null && !name.trim().isEmpty()) {
            if (!name.trim().equalsIgnoreCase(friendUser.getName())) {
                throw new IllegalArgumentException("Name does not match");
            }
        }
        if (friendUser.getId() == userId) {
            throw new IllegalArgumentException("Cannot add yourself");
        }
        if (!friendRepository.existsByUserIdAndFriendId(userId, friendUser.getId())) {
            friendRepository.save(new Friend(userId, friendUser.getId()));
        }
        if (!friendRepository.existsByUserIdAndFriendId(friendUser.getId(), userId)) {
            friendRepository.save(new Friend(friendUser.getId(), userId));
        }
        return friendUser;
    }

    public List<User> getFriends(int userId) {
        return userRepository.findFriendsForUser(userId);
    }

    public void removeFriend(int userId, int friendId) {
        friendRepository.deleteByUserIdAndFriendId(userId, friendId);
    }

    public User inviteFriend(int userId, String name, String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email required");
        }
        String emailTrimmed = email.trim();
        if (emailTrimmed.isEmpty()) {
            throw new IllegalArgumentException("Email required");
        }
        String nameTrimmed = name == null ? "" : name.trim();
        User friendUser = userRepository.findByEmailIgnoreCase(emailTrimmed).orElse(null);
        if (friendUser == null) {
            User newUser = new User();
            newUser.setName(nameTrimmed);
            newUser.setEmail(emailTrimmed);
            newUser.setPassword("123456");
            friendUser = userRepository.save(newUser);
            String token = java.util.UUID.randomUUID().toString();
            Invitation inv = new Invitation();
            inv.setUserId(friendUser.getId());
            inv.setEmail(emailTrimmed);
            inv.setToken(token);
            inv.setStatus("PENDING");
            inv.setExpiresAt(java.time.Instant.now().plus(java.time.Duration.ofDays(2)));
            invitationRepository.save(inv);
            try {
                org.springframework.mail.javamail.JavaMailSender sender = mailSenderProvider.getIfAvailable();
                if (sender != null) {
                    jakarta.mail.internet.MimeMessage message = sender.createMimeMessage();
                    org.springframework.mail.javamail.MimeMessageHelper helper = new org.springframework.mail.javamail.MimeMessageHelper(message, true);
                    helper.setTo(emailTrimmed);
                    helper.setSubject("Confirm your Expense Distributer account");
                    String link = "http://localhost:3000/register?token=" + token;
                    helper.setText("Hello " + nameTrimmed + ", please confirm your account by clicking: " + link, false);
                    sender.send(message);
                }
            } catch (Exception ignored) {}
        } else {
            if (!nameTrimmed.isEmpty() && !nameTrimmed.equalsIgnoreCase(friendUser.getName())) {
                throw new IllegalArgumentException("Name does not match");
            }
        }
        if (friendUser.getId() == userId) {
            throw new IllegalArgumentException("Cannot add yourself");
        }
        if (!friendRepository.existsByUserIdAndFriendId(userId, friendUser.getId())) {
            friendRepository.save(new Friend(userId, friendUser.getId()));
        }
        return friendUser;
    }

    public String confirmRegistration(String token) {
        Invitation inv = invitationRepository.findByToken(token).orElseThrow(() -> new IllegalArgumentException("Invalid token"));
        inv.setStatus("CONFIRMED");
        invitationRepository.save(inv);
        return "Confirmed";
    }
}
