package com.orderservice.order.repository;

import com.orderservice.order.entity.Eligibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EligibilityRepository extends JpaRepository<Eligibility, UUID> {
}
