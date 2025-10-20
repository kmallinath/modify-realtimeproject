package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.entities.Organization;
import com.booking.usermanagement.entities.OrganizationTherapy;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.entities.User;
import com.booking.usermanagement.repository.OrganizationRepository;
import com.booking.usermanagement.repository.OrganizationTherapyRepository;
import com.booking.usermanagement.repository.TherapyRepository;
import com.booking.usermanagement.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationTherapyService {
    private final OrganizationTherapyRepository orgTherapyRepository;
    private final OrganizationRepository organizationRepository;
    private final TherapyRepository therapyRepository;
    private final UserRepo userRepository;

    /**
     * Add new therapies to an organization (does NOT replace existing ones)
     */
    @Transactional
    public List<OrganizationTherapy> qualifyTherapies(UUID orgId, List<UUID> therapyIds, String qualifiedBy) {
        // Validate organization exists
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + orgId));

        // Get existing qualified therapy IDs
        List<UUID> existingTherapyIds = orgTherapyRepository
                .findQualifiedTherapyIdsByOrganizationId(orgId);

        // Filter out therapies that are already qualified
        List<UUID> newTherapyIds = therapyIds.stream()
                .filter(id -> !existingTherapyIds.contains(id))
                .collect(Collectors.toList());

        if (newTherapyIds.isEmpty()) {
            throw new IllegalArgumentException("All provided therapies are already qualified for this organization");
        }

        // Validate all new therapy IDs exist
        List<Therapy> therapies = therapyRepository.findAllById(newTherapyIds);
        if (therapies.size() != newTherapyIds.size()) {
            List<UUID> foundIds = therapies.stream().map(Therapy::getId).collect(Collectors.toList());
            List<UUID> missingIds = newTherapyIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toList());
            throw new IllegalArgumentException("Invalid therapy IDs: " + missingIds);
        }

        // Create new OrganizationTherapy relationships

        User qualifiedByUser = userRepository.findByEmail(qualifiedBy)
                .orElseThrow(() -> new IllegalArgumentException("Qualifier user not found: " + qualifiedBy));

        if(!qualifiedByUser.getRole().getName().equals("SPONSORADMIN")){
            throw new IllegalArgumentException("Only users with the SPONSOR role can qualify therapies");
        }

        List<OrganizationTherapy> newQualifications = therapies.stream()
                .map(therapy -> {
                    OrganizationTherapy orgTherapy = new OrganizationTherapy();
                    orgTherapy.setOrganization(org);
                    orgTherapy.setTherapy(therapy);
                    orgTherapy.setQualifiedBy(qualifiedByUser.getEmail());
                    orgTherapy.setQualifiedAt(LocalDateTime.now());
                    orgTherapy.setStatus("ACTIVE");
                    return orgTherapy;
                })
                .collect(Collectors.toList());

        // Save and return
        try {
            return orgTherapyRepository.saveAll(newQualifications);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Failed to qualify therapies due to duplicate mappings or invalid data", e);
        }
    }

    /**
     * Get all qualified therapies for an organization
     */
    @Transactional(readOnly = true)
    public List<Therapy> getQualifiedTherapies(UUID orgId) {
        // Validate organization exists
        if (!organizationRepository.existsById(orgId)) {
            throw new IllegalArgumentException("Organization not found with ID: " + orgId);
        }
        return orgTherapyRepository.findQualifiedTherapiesByOrganizationId(orgId);
    }

    /**
     * Replace ALL qualified therapies for an organization (use with caution)
     */
    @Transactional
    public List<OrganizationTherapy> replaceQualifiedTherapies(UUID orgId, List<UUID> therapyIds, String qualifiedBy) {
        Organization org = organizationRepository.findById(orgId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found with ID: " + orgId));

        // Delete all existing qualifications
        List<UUID> existingIds = orgTherapyRepository.findQualifiedTherapyIdsByOrganizationId(orgId);
        if (!existingIds.isEmpty()) {
            orgTherapyRepository.deleteByOrganizationIdAndTherapyIdIn(orgId, existingIds);
        }

        // Add new qualifications
        List<Therapy> therapies = therapyRepository.findAllById(therapyIds);
        if (therapies.size() != therapyIds.size()) {
            throw new IllegalArgumentException("Some therapy IDs are invalid");
        }

        List<OrganizationTherapy> newQualifications = therapies.stream()
                .map(therapy -> {
                    OrganizationTherapy orgTherapy = new OrganizationTherapy();
                    orgTherapy.setOrganization(org);
                    orgTherapy.setTherapy(therapy);
                    orgTherapy.setQualifiedBy(qualifiedBy);
                    orgTherapy.setQualifiedAt(LocalDateTime.now());
                    orgTherapy.setStatus("ACTIVE");// Set qualified field to comply with NOT NULL constraint
                    return orgTherapy;
                })
                .collect(Collectors.toList());

        try {
            return orgTherapyRepository.saveAll(newQualifications);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Failed to replace therapies due to duplicate mappings or invalid data", e);
        }
    }

    /**
     * Disqualify (remove) specific therapies from an organization
     */
    @Transactional
    public void disqualifyTherapies(UUID orgId, List<UUID> therapyIds) {
        if (!organizationRepository.existsById(orgId)) {
            throw new IllegalArgumentException("Organization not found with ID: " + orgId);
        }

        orgTherapyRepository.deleteByOrganizationIdAndTherapyIdIn(orgId, therapyIds);
    }
}