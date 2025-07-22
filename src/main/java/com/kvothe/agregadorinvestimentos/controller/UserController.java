package com.kvothe.agregadorinvestimentos.controller;

import com.kvothe.agregadorinvestimentos.dto.AccountDTO;
import com.kvothe.agregadorinvestimentos.dto.AccountResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.UserDTO;
import com.kvothe.agregadorinvestimentos.dto.UserResponseDTO;
import com.kvothe.agregadorinvestimentos.entity.Account;
import com.kvothe.agregadorinvestimentos.entity.BillingAddress;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController()
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTO request){
        var userId = userService.createUser(request);
        return ResponseEntity.created(URI.create("/v1/users/" + userId.toString())).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        try {
            var user = userService.getUserByID(userId);
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUserById() {
        var users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("userId") String userId){
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUserById(@PathVariable("userId") String userId,
                                               @RequestBody UserDTO request) {
        try {
            if(userService.userExists(userId)){
                var user = userService.updateUserById(userId, request);
                return ResponseEntity.ok(user);
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/{userId}/accounts")
    public ResponseEntity<BillingAddress> createAccount(@PathVariable String userId, @RequestBody AccountDTO resquest){
        userService.createAccount(userId, resquest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/accounts")
    public ResponseEntity<List<AccountResponseDTO>> getAccount(@PathVariable String userId){
        var accounts = userService.getAllAccounts(userId);
        return ResponseEntity.ok(accounts);
    }
}
