package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.entities.Nurse;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.repository.NurseRepository;
import com.booking.usermanagement.repository.OrganizationRepository;
import com.booking.usermanagement.repository.OrganizationTherapyRepository;
import com.booking.usermanagement.repository.TherapyRepository;
import com.booking.usermanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NurseService {
    private final NurseRepository nurseRepository;
    private final OrganizationRepository organizationRepository;
    private final TherapyRepository therapyRepository;
    private final OrganizationTherapyRepository orgTherapyRepository;
    private  final UserService userService;

    public Nurse onboardNurse(String name, String email, String phone, UUID orgId, List<UUID> therapyIds) {
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
        nurseUserDto.setPassword(phone);
        nurseUserDto.setRoleId(2L); // Assuming 3 is the role ID for Nurse
        userService.registerUser(nurseUserDto);


        Nurse nurse = new Nurse();
        nurse.setName(name);
        nurse.setEmail(email);
        nurse.setPhone(phone);
        nurse.setOrganization(org);
        nurse.setTherapies(therapies);

        return nurseRepository.save(nurse);
    }

    public List<Nurse> getAllNurses() {
        return nurseRepository.findAll();
    }
}
