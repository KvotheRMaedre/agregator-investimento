package com.kvothe.agregadorinvestimentos.controller;

import com.kvothe.agregadorinvestimentos.dto.CreateUserDTO;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.services.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController()
@RequestMapping("/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserDTO request){
        var userId = userService.createUser(request);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathParam("userId") UUID userId){
        return null;
    }
}
