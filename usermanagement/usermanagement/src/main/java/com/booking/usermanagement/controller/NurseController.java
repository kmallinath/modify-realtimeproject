package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.NurseOnboardRequest;
import com.booking.usermanagement.entities.Nurse;
import com.booking.usermanagement.service.ServiceImpl.NurseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/nurses")
@RequiredArgsConstructor
public class NurseController {
    private final NurseService nurseService;

    @PostMapping("/onboard")
    public Nurse onboard(@RequestBody NurseOnboardRequest req) {
        return nurseService.onboardNurse(req.getName(), req.getEmail(), req.getPhone(),
                req.getOrganizationId(), req.getTherapyIds());
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Nurse>> getAllNurses() {
        List<Nurse> nurses = nurseService.getAllNurses();
        return ResponseEntity.ok(nurses);
    }
}
