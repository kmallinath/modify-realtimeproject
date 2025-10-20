package com.booking.usermanagement.util;

import com.booking.usermanagement.dtos.RoleDto;
import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.entities.Role;
import com.booking.usermanagement.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());


        user.setCreatedAt(userDto.getCreatedAt());
        user.setUpdatedAt(userDto.getUpdatedAt());
//        user.setCreatedBy(null);
//        user.setUpdatedBy(null);
        return user;

    }
    public  UserDto entityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        userDto.setRoleId(user.getRole().getId());
        userDto.setRoleName(user.getRole().getName());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        userDto.setCreatedBy(user.getCreatedBy());
        userDto.setUpdatedBy(user.getUpdatedBy());
        return userDto;
    }


    public RoleDto roleToRoleDto(Role savedRole) {
        RoleDto roleDto = new RoleDto();
        roleDto.setName(savedRole.getName());
        roleDto.setDescription(savedRole.getDescription());
        return roleDto;

    }

    public Role roleDtotoRole(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        role.setDescription(roleDto.getDescription());
        return role;
    }
}
