package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.RoleDto;
import com.booking.usermanagement.entities.Role;
import com.booking.usermanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/add")
    public ResponseEntity<RoleDto> addRole(@RequestBody RoleDto role) {
        RoleDto savedRole = roleService.addNewRole(role);
        return ResponseEntity.ok(savedRole);
    }

}
