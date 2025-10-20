package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.QualifyTherapiesRequest;
import com.booking.usermanagement.entities.OrganizationTherapy;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.service.ServiceImpl.OrganizationTherapyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/organizations/{orgId}/therapies")
@RequiredArgsConstructor
public class OrganizationTherapyController {
    private final OrganizationTherapyService service;

    /**
     * Add new therapies to an organization (does NOT replace existing)
     * POST /api/organizations/{orgId}/therapies/qualify
     */
    @PostMapping("qualify")
    public ResponseEntity<List<OrganizationTherapy>> qualifyTherapies(
            @PathVariable UUID orgId,
            @Valid @RequestBody QualifyTherapiesRequest request
    ) {
        List<OrganizationTherapy> result = service.qualifyTherapies(
                orgId,
                request.getTherapyIds(),
                request.getQualifiedBy()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * Get all qualified therapies for an organization
     * GET /api/organizations/{orgId}/therapies/qualified
     */
    @GetMapping("/qualified")
    public ResponseEntity<List<Therapy>> getQualifiedTherapies(@PathVariable UUID orgId) {
        return ResponseEntity.ok(service.getQualifiedTherapies(orgId));
    }

    /**
     * Replace ALL qualified therapies (use with caution)
     * PUT /api/organizations/{orgId}/therapies/qualify
     */
    @PutMapping("/qualify")
    public ResponseEntity<List<OrganizationTherapy>> replaceQualifiedTherapies(
            @PathVariable UUID orgId,
            @Valid @RequestBody QualifyTherapiesRequest request
    ) {
        List<OrganizationTherapy> result = service.replaceQualifiedTherapies(
                orgId,
                request.getTherapyIds(),
                request.getQualifiedBy()
        );
        return ResponseEntity.ok(result);
    }

    /**
     * Disqualify (remove) specific therapies
     * DELETE /api/organizations/{orgId}/therapies/qualify
     */
    @DeleteMapping("/qualify")
    public ResponseEntity<Void> disqualifyTherapies(
            @PathVariable UUID orgId,
            @RequestBody List<UUID> therapyIds
    ) {
        service.disqualifyTherapies(orgId, therapyIds);
        return ResponseEntity.noContent().build();
    }
}
