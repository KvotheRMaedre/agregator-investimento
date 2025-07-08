package com.kvothe.agregadorinvestimentos.services;

import com.kvothe.agregadorinvestimentos.dto.UserDTO;
import com.kvothe.agregadorinvestimentos.entity.User;
import com.kvothe.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success.")
        void shouldCreateUserWithSuccess(){
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            var userDTO = new UserDTO(
                    "user",
                    "email@email.com",
                    "password"
            );
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            var response = userService.createUser(userDTO);
            var userCaptured = userArgumentCaptor.getValue();

            assertNotNull(response);
            assertEquals(userDTO.name(), userCaptured.getName());
            assertEquals(userDTO.email(), userCaptured.getEmail());
            assertEquals(userDTO.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs(){
            var userDTO = new UserDTO(
                    "user",
                    "email@email.com",
                    "password"
            );
            doThrow(new RuntimeException())
                    .when(userRepository)
                    .save(any());

            assertThrows(RuntimeException.class, () -> userService.createUser(userDTO));
        }
    }

    @Nested
    class getUserById{

        @Test
        @DisplayName("Should get user by id with success.")
        void shouldGetUserByIdWithSuccess() {
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            var response = userService.getUserByID(user.getId().toString());

            assertTrue(response.isPresent());
            assertEquals(user.getId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty.")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();
            doReturn(Optional.empty())
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());

            var response = userService.getUserByID(userId.toString());

            assertFalse(response.isPresent());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class getAllUsers{

        @Test
        @DisplayName("Should return all users with success when theres only 1 user")
        void shouldReturnAllUsersWithSuccessWhenTheresOnlyOneUser(){
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            doReturn(List.of(user))
                    .when(userRepository)
                    .findAll();

            var response = userService.getAllUsers();

            assertNotNull(response);
            assertEquals(1, response.size());
        }

        @Test
        @DisplayName("Should return all users with success when theres more than 1 user")
        void shouldReturnAllUsersWithSuccessWhenTheresOnlyMoreThanOneUser(){
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            var user2 = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            var userList = List.of(user, user2);
            doReturn(userList)
                    .when(userRepository)
                    .findAll();

            var response = userService.getAllUsers();

            assertNotNull(response);
            assertEquals(userList.size(), response.size());
        }

        @Test
        @DisplayName("Should return all users with success when theres none user")
        void shouldReturnAllUsersWithSuccessWhenTheresNoneUser(){
            doReturn(List.of())
                    .when(userRepository)
                    .findAll();

            var response = userService.getAllUsers();

            assertNotNull(response);
            assertEquals(0, response.size());
        }
    }

    @Nested
    class userExists{

        @Test
        @DisplayName("Should return true when user exists")
        void shouldReturnTrueWhenUserExists() {
            var userId = UUID.randomUUID();
            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            var response = userService.userExists(userId.toString());

            assertTrue(response);
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should return false when user doesn't exists")
        void shouldReturnFalseWhenUserDoesntExists() {
            var userId = UUID.randomUUID();
            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            var response = userService.userExists(userId.toString());

            assertFalse(response);
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }

    @Nested
    class deleteUserById{

        @Test
        @DisplayName("Should delete user with success")
        void shouldDeleteUserWithSuccess() {
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );

            doReturn(true)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            doNothing()
                    .when(userRepository)
                            .deleteById(uuidArgumentCaptor.capture());

            userService.deleteUserById(user.getId().toString());

            var listId = uuidArgumentCaptor.getAllValues();
            assertEquals(user.getId(), listId.getFirst());
            assertEquals(user.getId(), listId.getLast());
            verify(userRepository, times(1)).existsById(listId.getFirst());
            verify(userRepository, times(1)).deleteById(listId.getLast());
        }

        @Test
        @DisplayName("Should do nothing if the user doesn't exists")
        void shouldNotDeleteUserIfDoesntExist() {
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );

            doReturn(false)
                    .when(userRepository)
                    .existsById(uuidArgumentCaptor.capture());

            userService.deleteUserById(user.getId().toString());

            assertEquals(user.getId(), uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById{

        @Test
        @DisplayName("Should update user by id when user exists")
        void shouldUpdateUserByIdWhenUserExists(){
            var user = new User(
                    UUID.randomUUID(),
                    "user",
                    "email@email.com",
                    "password", Instant.now(),
                    null
            );
            var userDTO = new UserDTO(
                    "updated",
                    "updated@email.com",
                    "updated"
            );
            doReturn(Optional.of(user))
                    .when(userRepository)
                    .findById(uuidArgumentCaptor.capture());
            doReturn(user)
                    .when(userRepository)
                    .save(userArgumentCaptor.capture());

            var response = userService.updateUserById(user.getId().toString(), userDTO);
            var userCaptured = userArgumentCaptor.getValue();

            assertNotNull(response);
            assertEquals(userDTO.name(), userCaptured.getName());
            assertEquals(userDTO.email(), userCaptured.getEmail());
            assertEquals(userDTO.password(), userCaptured.getPassword());
            assertEquals(userDTO.name(), response.getName());
            assertEquals(userDTO.email(), response.getEmail());
            assertEquals(userDTO.password(), response.getPassword());
        }

    }
}