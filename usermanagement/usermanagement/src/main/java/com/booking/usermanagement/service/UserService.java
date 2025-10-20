package com.booking.usermanagement.service;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.entities.User;
import com.booking.usermanagement.repository.UserRepo;
import jakarta.persistence.Access;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;


public interface UserService {

    public UserDto registerUser(UserDto user);
    public UserDto getUserById(UUID id);
    public UserDto updateUser(UserDto user);
    public boolean deleteUserById(UUID id);
    public UserDto getUserByEmail(String email);

}
