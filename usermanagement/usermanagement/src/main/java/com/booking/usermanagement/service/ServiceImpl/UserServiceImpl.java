package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.UserDto;
import com.booking.usermanagement.dtos.UserOnboardedEvent;
import com.booking.usermanagement.dtos.ValidationUserDto;
import com.booking.usermanagement.entities.Role;
import com.booking.usermanagement.entities.User;
import com.booking.usermanagement.exception.ResourceAlreadyExists;
import com.booking.usermanagement.exception.ResourceNotFoundException;
import com.booking.usermanagement.repository.RoleRepo;
import com.booking.usermanagement.repository.UserRepo;
import com.booking.usermanagement.service.UserOnboardingEventService;
import com.booking.usermanagement.service.UserService;
import com.booking.usermanagement.util.DtoMapper;
import com.booking.usermanagement.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DtoMapper dtoMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserOnboardingEventService userOnboardingEventService;

    @Override
    @Transactional
    public UserDto registerUser(UserDto userDto) {
        log.info("Registering user with email: {}", userDto.getEmail());
        String email = userDto.getEmail();

        // Check if user already exists
        Optional<User> existingUser = userRepo.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new ResourceAlreadyExists("Email already in use");
        }

        // Create user entity
        User user = dtoMapper.dtoToUser(userDto);
        long roleId = userDto.getRoleId();
        Role role = roleRepo.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));

        user.setRole(role);
        user.setStatus("PENDING EMAIL CONFIRMATION");

        // Save user first
        User savedUser = userRepo.save(user);
        userRepo.flush();

        // Send activation code
        sendActivationCode(savedUser);

        log.info("User registered successfully: {}", email);
        return dtoMapper.entityToDto(savedUser);
    }

    @Override
    public UserDto getUserById(String id) {
        User user = userRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return dtoMapper.entityToDto(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        UUID userId = userDto.getId();
        User existingUser = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Update name if provided
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        // Cannot change email
        if (userDto.getEmail() != null) {
            throw new IllegalArgumentException("Cannot change email");
        }

        // Update password if provided
        if (userDto.getPassword() != null) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        // Update role if provided (but not to admin)
        if (userDto.getRoleId() != null) {
            long roleId = userDto.getRoleId();
            if (roleId == 1L) {
                throw new IllegalArgumentException("Cannot change to admin role");
            }
            Role role = roleRepo.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", roleId));
            existingUser.setRole(role);
        }

        User updatedUser = userRepo.save(existingUser);
        return dtoMapper.entityToDto(updatedUser);
    }

    @Override
    public boolean deleteUserById(UUID id) {
        User existingUser = userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        userRepo.delete(existingUser);
        return true;
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return dtoMapper.entityToDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(dtoMapper::entityToDto)
                .toList();
    }

    @Override
    public ValidationUserDto validateUser(String username) {
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        ValidationUserDto responseDto = new ValidationUserDto();
        responseDto.setUsername(user.getEmail());
        responseDto.setRoleName(user.getRole().getName());
        return responseDto;
    }

    @Override
    public String loginUser(String username, String password) {
        // Find user
        User user = userRepo.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", username));

        // Check if account is active
        if (!"ACTIVE".equals(user.getStatus())) {
            return "ACCOUNT_NOT_ACTIVATED";
        }

        // Validate password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            return "INVALID_CREDENTIALS";
        }

        // Generate and return JWT token
        return jwtUtil.generateToken(user.getId(), user.getRole().getName(), username);
    }

    @Override
    public String activateAccount(String email, String password,String code) {
        User savedUser = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Check if already active
        if ("ACTIVE".equals(savedUser.getStatus())) {
            return "ACCOUNT_ALREADY_ACTIVATED";
        }

        // Validate code
        if (!isValidCode(savedUser, code)) {
            return "INVALID_VERIFICATION_CODE";
        }

        // Check expiry
        if (savedUser.getCodeExpiresAt() == null || savedUser.getCodeExpiresAt().before(new Date())) {
            return "VERIFICATION_CODE_EXPIRED";
        }

        // Activate account
        savedUser.setStatus("ACTIVE");
        savedUser.setVerificationCode(null);
        savedUser.setCodeExpiresAt(null);
        savedUser.setPassword(passwordEncoder.encode(password));
        userRepo.save(savedUser);

        log.info("Account activated successfully: {}", email);
        return "ACCOUNT_ACTIVATED_SUCCESSFULLY";
    }

    @Override
    public void sendActivationCode(User user) {
        // Generate 6-digit code
        SecureRandom secureRandom = new SecureRandom();
        int verificationCode = secureRandom.nextInt(900000) + 100000;

        // Store code in database with expiry
        user.setVerificationCode(String.valueOf(verificationCode));
        user.setCodeExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30 minutes
        userRepo.save(user);

        // Send via Kafka
        UserOnboardedEvent userOnboardedEvent = new UserOnboardedEvent();
        userOnboardedEvent.setEmail(user.getEmail());
        userOnboardedEvent.setName(user.getName());
        userOnboardedEvent.setVeridicationCode(verificationCode);
        userOnboardingEventService.publishUserOnboardedEvent(userOnboardedEvent);

        log.info("Activation code sent to: {}", user.getEmail());
    }

    @Override
    public String forgotPassword(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Check if account is active
        if (!"ACTIVE".equals(user.getStatus())) {
            return "USER_ACCOUNT_NOT_ACTIVE";
        }

        // Send reset code
        sendPasswordResetCode(user);

        log.info("Password reset code sent to: {}", email);
        return "PASSWORD_RESET_CODE_SENT_TO_EMAIL";
    }

    @Override
    public String resetPassword(String email, String code, String newPassword) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        // Validate code
        if (!isValidCode(user, code)) {
            return "INVALID_VERIFICATION_CODE";
        }

        // Check expiry
        if (user.getCodeExpiresAt() == null || user.getCodeExpiresAt().before(new Date())) {
            return "VERIFICATION_CODE_EXPIRED";
        }

        // Reset password
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerificationCode(null);
        user.setCodeExpiresAt(null);
        userRepo.save(user);

        log.info("Password reset successfully for: {}", email);
        return "PASSWORD_RESET_SUCCESSFULLY";
    }

    /**
     * Send password reset code to user
     */
    private void sendPasswordResetCode(User user) {
        // Generate 6-digit code
        SecureRandom secureRandom = new SecureRandom();
        int verificationCode = secureRandom.nextInt(900000) + 100000;

        // Store code in database with expiry
        user.setVerificationCode(String.valueOf(verificationCode));
        user.setCodeExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000)); // 30 minutes
        userRepo.save(user);

        // Send via Kafka (you can create a separate event type for password reset)
        UserOnboardedEvent resetEvent = new UserOnboardedEvent();
        resetEvent.setEmail(user.getEmail());
        resetEvent.setName(user.getName());
        resetEvent.setVeridicationCode(verificationCode);
        userOnboardingEventService.publishUserOnboardedEvent(resetEvent);

        log.info("Password reset code sent to: {}", user.getEmail());
    }

    /**
     * Validate verification code
     */
    private boolean isValidCode(User user, String code) {
        return user.getVerificationCode() != null &&
                user.getVerificationCode().equals(code);
    }
}