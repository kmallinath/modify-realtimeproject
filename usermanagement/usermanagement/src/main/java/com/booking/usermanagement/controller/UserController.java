package com.booking.usermanagement.controller;


import com.booking.usermanagement.dtos.LoginDto;
import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.dtos.ValidationUserDto;
import com.booking.usermanagement.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    //apis to update user details, delete user, get user by id, get all users, register user, login user, logout user

    // register user
    @PostMapping("/register")
    @PreAuthorize("hasRole('SPONSORADMIN')")
    public ResponseEntity<UserDto> registerUser(@RequestBody UserDto userDto) {
        log.info("Controller: Received registration request for email: {}", userDto.getEmail());
        UserDto savedUserDto=userService.registerUser(userDto);
        return ResponseEntity.ok(savedUserDto);
    }

    @GetMapping("/getbyid/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") UUID id) {
        UserDto userDto = userService.getUserById(String.valueOf(id));
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") UUID id, @RequestBody UserDto userDto) {
        userDto.setId(id);
        UserDto updatedUserDto = userService.updateUser(userDto);
        return ResponseEntity.ok(updatedUserDto);
    }
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SPONSORADMIN')")
    public ResponseEntity<String> deleteUserById(@PathVariable("id") UUID id) {
        boolean isDeleted = userService.deleteUserById(id);
        if (isDeleted) {
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.status(500).body("Failed to delete user");
        }
    }

    @GetMapping("/getbyemail/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable("email") String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/validate")
    public ResponseEntity<ValidationUserDto> validateUser(@RequestParam String userName) {
        ValidationUserDto validatedUser = userService.validateUser(userName);
        return ResponseEntity.ok(validatedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto userDto) {
        String token = userService.loginUser(userDto.getEmail(), userDto.getPassword());
        return ResponseEntity.ok(token);
    }
}
