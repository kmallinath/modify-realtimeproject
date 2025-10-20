package com.booking.usermanagement.repository;

import com.booking.usermanagement.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepo  extends JpaRepository<User, UUID> {


    Optional<User> findByEmail(String email);

}
