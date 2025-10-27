package com.booking.usermanagement.service;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.dtos.ValidationUserDto;

import java.util.List;
import java.util.UUID;


public interface UserService {

    public UserDto registerUser(UserDto user);
    public UserDto getUserById(String id);
    public UserDto updateUser(UserDto user);
    public boolean deleteUserById(UUID id);
    public UserDto getUserByEmail(String email);

    List<UserDto> getAllUsers();

    ValidationUserDto validateUser(String validationUserDto);
}
