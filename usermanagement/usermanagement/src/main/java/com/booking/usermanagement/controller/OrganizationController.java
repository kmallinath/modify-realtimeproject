package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.OrganizationDto;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.service.ServiceImpl.OrganizationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationServiceImpl organizationService;

    @PostMapping(value = "/create",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationDto> createOrganization(@RequestBody OrganizationDto organization) {
        // Implementation for creating an organization

        OrganizationDto organizationDto=organizationService.createOrganization(organization);
        return ResponseEntity.ok(organizationDto);


    }

    @GetMapping("/getall")
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        List<Organization> organizations = organizationService.getAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

}
