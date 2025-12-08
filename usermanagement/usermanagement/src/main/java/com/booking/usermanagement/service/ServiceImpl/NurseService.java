package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.*;
import com.booking.usermanagement.entities.Nurse;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.entities.User;
import com.booking.usermanagement.repository.NurseRepository;
import com.booking.usermanagement.repository.OrganizationRepository;
import com.booking.usermanagement.repository.OrganizationTherapyRepository;
import com.booking.usermanagement.repository.TherapyRepository;
import com.booking.usermanagement.service.UserOnboardingEventService;
import com.booking.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NurseService {
    private final NurseRepository nurseRepository;
    private final OrganizationRepository organizationRepository;
    private final TherapyRepository therapyRepository;
    private final OrganizationTherapyRepository orgTherapyRepository;
    private  final UserService userService;
    private final UserOnboardingEventService userOnboardingEventService;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public NurseDto onboardNurse(String name, String email, String phone, UUID orgId, List<UUID> therapyIds) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // Validate therapies
        List<UUID> allowedTherapyIds = orgTherapyRepository.findQualifiedTherapyIdsByOrganizationId(orgId);
        if (!allowedTherapyIds.containsAll(therapyIds)) {
            throw new IllegalArgumentException("Some therapies are not approved for this organization");
        }

        List<Therapy> therapies = therapyRepository.findAllById(therapyIds);

        //Onboard User With Nurse Role
        UserDto nurseUserDto = new UserDto();
        nurseUserDto.setName(name);
        nurseUserDto.setEmail(email);
        nurseUserDto.setRoleId(2L); //  2 is the role ID for Nurse
         // Set a default password or generate one
        UserDto savedUser=userService.registerUser(nurseUserDto);

        Nurse nurse = new Nurse();
        nurse.setId(savedUser.getId());
        nurse.setName(name);
        nurse.setEmail(email);
        nurse.setPhone(phone);
        nurse.setOrganization(org);
        nurse.setTherapies(therapies);
        Nurse savedNurse = nurseRepository.save(nurse);
        NurseDto nurseDto=modelMapper.map(savedNurse, NurseDto.class);
        nurseDto.setOrganizationDto(modelMapper.map(org, OrganizationDto.class));
        nurseDto.setTherapyNames(therapies.stream()
                .map(therapy -> modelMapper.map(therapy, TherapyDto.class))
                .toList());
        return nurseDto;

    }

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }

    public NurseDto getNurseById(UUID id) {

        Nurse nurse= nurseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found"));

        OrganizationDto organizationDto=modelMapper.map( nurse.getOrganization(), OrganizationDto.class);
        modelMapper.map(nurse.getTherapies(), TherapyDto.class);
        NurseDto nurseDto=modelMapper.map(nurse, NurseDto.class);
        nurseDto.setOrganizationDto(organizationDto);
        nurseDto.setTherapyNames(nurse.getTherapies().stream()
                .map(therapy -> modelMapper.map(therapy, TherapyDto.class))
                .toList());
        return  nurseDto;
    }

    public NurseDto getNurseByEmail(String email) {
        Nurse nurse= nurseRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Nurse not found"));

        OrganizationDto organizationDto=modelMapper.map( nurse.getOrganization(), OrganizationDto.class);
        modelMapper.map(nurse.getTherapies(), TherapyDto.class);
        NurseDto nurseDto=modelMapper.map(nurse, NurseDto.class);
        nurseDto.setOrganizationDto(organizationDto);
        nurseDto.setTherapyNames(nurse.getTherapies().stream()
                .map(therapy -> modelMapper.map(therapy, TherapyDto.class))
                .toList());
        return  nurseDto;
    }
}
