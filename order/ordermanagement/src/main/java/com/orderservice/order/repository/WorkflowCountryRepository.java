package com.orderservice.order.repository;

import com.orderservice.order.entity.CurrentCountryWorkflowMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkflowCountryRepository extends JpaRepository<CurrentCountryWorkflowMapping, UUID> {

    Optional<CurrentCountryWorkflowMapping> findByCountryCode(String countryCode);
}
