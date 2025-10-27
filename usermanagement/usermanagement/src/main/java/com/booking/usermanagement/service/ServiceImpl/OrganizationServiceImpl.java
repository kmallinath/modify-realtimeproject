package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.config.ModelMapperConfig;
import com.booking.usermanagement.dtos.OrganizationDto;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.exception.ResourceAlreadyExists;
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

        if(organizationDto.getName() == null || organizationDto.getName().isEmpty()) {
            throw new IllegalArgumentException("Organization name cannot be null or empty");
        }
        if(organizationRepository.findByName(organizationDto.getName()) != null) {
            throw new ResourceAlreadyExists("Organization with the given Name already exists");
        }
        Organization organization = modelMapper.map(organizationDto, Organization.class);

        Organization savedOrganization = organizationRepository.save(organization);

        OrganizationDto organizationDto1 = modelMapper.map(savedOrganization, OrganizationDto.class);
        organizationDto1.setId(savedOrganization.getId());
        System.out.println("Organization created with ID: " + savedOrganization.getId());
        return organizationDto1;

    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }
}
