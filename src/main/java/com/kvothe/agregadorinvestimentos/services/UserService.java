package com.kvothe.agregadorinvestimentos.services;

import com.kvothe.agregadorinvestimentos.dto.CreateUserDTO;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UUID createUser(CreateUserDTO createUserDTO){
        var user = new User(
                null,
                createUserDTO.name(),
                createUserDTO.email(),
                createUserDTO.password(),
                Instant.now(),
                null);
        var savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
