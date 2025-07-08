package com.kvothe.agregadorinvestimentos.services;

import com.kvothe.agregadorinvestimentos.dto.UserDTO;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(UserDTO userDTO){
        var user = new User(
                null,
                userDTO.name(),
                userDTO.email(),
                userDTO.password(),
                Instant.now(),
                null);
        var savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    public Optional<User> getUserByID(String userId){
        return userRepository.findById(UUID.fromString(userId));
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public boolean userExists(String userId){
        return userRepository.existsById(UUID.fromString(userId));
    }

    public void deleteUserById(String userId){
        if(userExists(userId)){
            userRepository.deleteById(UUID.fromString(userId));
        }
    }

    public User updateUserById(String userId, UserDTO userDTO){
        var userUpdated = userRepository.findById(UUID.fromString(userId)).get();
        userUpdated.setName(userDTO.name());
        userUpdated.setEmail(userDTO.email());
        userUpdated.setPassword(userDTO.password());
        return userRepository.save(userUpdated);
    }
}
