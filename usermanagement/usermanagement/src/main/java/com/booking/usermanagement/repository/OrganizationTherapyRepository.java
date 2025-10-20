package com.booking.usermanagement.repository;

import com.booking.usermanagement.entities.OrganizationTherapy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrganizationTherapyRepository extends JpaRepository<OrganizationTherapy, UUID> {

    // Find all qualified therapy IDs for an organization
    @Query("SELECT ot.therapy.id FROM OrganizationTherapy ot " +
            "WHERE ot.organization.id = :orgId AND ot.status = 'ACTIVE'")
    List<UUID> findQualifiedTherapyIdsByOrganizationId(@Param("orgId") UUID orgId);

    // Find all qualified therapies for an organization
    @Query("SELECT ot.therapy FROM OrganizationTherapy ot " +
            "WHERE ot.organization.id = :orgId AND ot.status = 'ACTIVE'")
    List<com.booking.usermanagement.entities.Therapy> findQualifiedTherapiesByOrganizationId(@Param("orgId") UUID orgId);

    // Check if a specific therapy is already qualified
    boolean existsByOrganizationIdAndTherapyIdAndStatus(UUID organizationId, UUID therapyId, String status);

    // Find specific organization-therapy relationship
    @Query("SELECT ot FROM OrganizationTherapy ot " +
            "WHERE ot.organization.id = :orgId AND ot.therapy.id = :therapyId")
    List<OrganizationTherapy> findByOrganizationIdAndTherapyId(
            @Param("orgId") UUID orgId,
            @Param("therapyId") UUID therapyId
    );

    // Delete (disqualify) specific therapies
    void deleteByOrganizationIdAndTherapyIdIn(UUID organizationId, List<UUID> therapyIds);
}