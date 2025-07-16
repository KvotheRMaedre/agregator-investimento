package com.kvothe.agregadorinvestimentos.services;

import com.kvothe.agregadorinvestimentos.dto.AccountDTO;
import com.kvothe.agregadorinvestimentos.dto.AccountResponseDTO;
import com.kvothe.agregadorinvestimentos.dto.UserDTO;
import com.kvothe.agregadorinvestimentos.entity.Account;
import com.kvothe.agregadorinvestimentos.entity.BillingAddress;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.repository.AccountRepository;
import com.kvothe.agregadorinvestimentos.repository.BillingAddressRepository;
import com.kvothe.agregadorinvestimentos.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final BillingAddressRepository billingAddressRepository;

    public UserService(UserRepository userRepository,
                       AccountRepository accountRepository,
                       BillingAddressRepository billingAddressRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.billingAddressRepository = billingAddressRepository;
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

    public void createAccount(String userId, AccountDTO accountDTO) {
        var user = getUserByID(userId);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        var account = new Account(
                null,
                accountDTO.description(),
                user.get(),
                null,
                new ArrayList<>()
        );

        var accountCreated = accountRepository.saveAndFlush(account);

        var billingAddress = new BillingAddress(
                null,
                accountDTO.street(),
                accountDTO.number(),
                accountCreated
        );

        billingAddressRepository.saveAndFlush(billingAddress);
    }

    public List<AccountResponseDTO> getAllAccounts(String userId) {
        var user = getUserByID(userId);
        if (user.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return user.get().getAccounts()
                .stream()
                .map(account -> new AccountResponseDTO(account.getAccountId().toString(), account.getDescription()))
                .toList();
    }
}
