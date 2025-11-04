package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.NurseDto;
import com.booking.usermanagement.dtos.NurseOnboardRequest;
import com.booking.usermanagement.entities.Nurse;
import com.booking.usermanagement.service.ServiceImpl.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/nurses")
@RequiredArgsConstructor
public class NurseController {
    private final NurseService nurseService;

    @PostMapping("/onboard")
    @PreAuthorize("hasRole('SPONSORADMIN')")
    public Nurse onboard(@RequestBody NurseOnboardRequest req) {
        return nurseService.onboardNurse(req.getName(), req.getEmail(), req.getPhone(),
                req.getOrganizationId(), req.getTherapyIds());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Nurse>> getAllNurses() {
        List<Nurse> nurses = nurseService.getAllNurses();
        return ResponseEntity.ok(nurses);
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<NurseDto> getNurseById(@PathVariable("id") UUID id) {
        NurseDto nurse = nurseService.getNurseById(id);
        return ResponseEntity.ok(nurse);
    }

    @GetMapping("/get/email/{email}")
    public ResponseEntity<NurseDto> getNurseByEmail(@PathVariable("email") String email) {
        NurseDto nurse = nurseService.getNurseByEmail(email);
        return ResponseEntity.ok(nurse);
    }
}
