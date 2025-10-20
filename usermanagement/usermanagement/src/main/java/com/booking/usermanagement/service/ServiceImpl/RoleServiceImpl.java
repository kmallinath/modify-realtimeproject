package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.RoleDto;
import com.booking.usermanagement.repository.RoleRepo;
import com.booking.usermanagement.service.RoleService;
import com.booking.usermanagement.util.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private DtoMapper dtoMapper;

    @Override
    public RoleDto addNewRole(RoleDto roleDto) {

        var role = dtoMapper.roleDtotoRole(roleDto);
        var savedRole = roleRepo.save(role);
        return dtoMapper.roleToRoleDto(savedRole);

    }
}
