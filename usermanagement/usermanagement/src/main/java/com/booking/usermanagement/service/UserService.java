package com.booking.usermanagement.service;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.dtos.ValidationUserDto;
import com.booking.usermanagement.entities.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDto registerUser(UserDto user);

    UserDto getUserById(String id);

    UserDto updateUser(UserDto user);

    boolean deleteUserById(UUID id);

    UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    ValidationUserDto validateUser(String username);

    String loginUser(String email, String password);

    String activateAccount(String email, String password,String code);

    void sendActivationCode(User user);

    String forgotPassword(String email);

    String resetPassword(String email, String code, String newPassword);
}
