package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.dtos.ValidationUserDto;
import com.booking.usermanagement.entities.Role;
import com.booking.usermanagement.entities.User;
import com.booking.usermanagement.exception.ResourceAlreadyExists;
import com.booking.usermanagement.exception.ResourceNotFoundException;
import com.booking.usermanagement.repository.RoleRepo;
import com.booking.usermanagement.repository.UserRepo;
import com.booking.usermanagement.service.UserService;
import com.booking.usermanagement.util.DtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;


    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private DtoMapper dtoMapper;





    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {

        log.info("Registering user with email: {}", userDto.getEmail());
        String email = userDto.getEmail();
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new ResourceAlreadyExists("Email already in use");
        }
        User user = dtoMapper.dtoToUser(userDto);
        long roleId = userDto.getRoleId();
        Role role= roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
        user.setRole(role);
        log.info("Raw password: {}", userDto.getPassword());
        log.info("Encoded password: {}", passwordEncoder.encode(userDto.getPassword()));

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = userRepo.save(user);
        return dtoMapper.entityToDto(savedUser);
    }

    @Override
    public UserDto getUserById(String id) {
       User user = userRepo.findById(UUID.fromString(id)).orElseThrow(()->new ResourceNotFoundException("User", "id", id));
       return dtoMapper.entityToDto(user);

    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UUID userId = userDto.getId();
        User existingUser = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        if(userDto.getName()!=null)  existingUser.setName(userDto.getName());
        if(userDto.getEmail()!=null)  throw new IllegalArgumentException("Cannot change email");
        if(userDto.getPassword()!=null)existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userDto.getRoleId()!=null) {
            long roleId = userDto.getRoleId();
            if(roleId!=1L){
                throw new IllegalArgumentException("Cannot change to admin role");
            }
            Role role= roleRepo.findById(roleId).orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
            existingUser.setRole(role);
        }
        User updatedUser = userRepo.save(existingUser);
        return dtoMapper.entityToDto(updatedUser);
    }

    @Override
    public boolean deleteUserById(UUID id) {
        User existingUser = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(existingUser);
        return true;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        Optional<User> userOptional = userRepo.findByEmail(email);
        if (userOptional.isPresent()) {
            return dtoMapper.entityToDto(userOptional.get());
        } else {
            throw new ResourceNotFoundException("User", "email", email);
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream().map(dtoMapper::entityToDto).toList();
    }

    @Override
    public ValidationUserDto validateUser(String username) {
        User user=userRepo.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        ValidationUserDto responseDto=new ValidationUserDto();
        responseDto.setUsername(user.getEmail());
        responseDto.setRoleName(user.getRole().getName());
        return responseDto;
    }
}
