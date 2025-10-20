package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.config.ModelMapperConfig;
import com.booking.usermanagement.dtos.OrganizationDto;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.repository.OrganizationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationServiceImpl {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public OrganizationDto createOrganization(OrganizationDto organizationDto) {
        // Implementation for creating an organization

        Organization organization = modelMapper.map(organizationDto, Organization.class);
        Organization savedOrganization = organizationRepository.save(organization);
        return modelMapper.map(savedOrganization, OrganizationDto.class);

    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
