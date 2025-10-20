package com.booking.usermanagement.repository;

import com.booking.usermanagement.entities.Nurse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NurseRepository extends JpaRepository<Nurse, UUID> {}