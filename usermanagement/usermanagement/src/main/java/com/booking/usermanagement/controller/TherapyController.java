package com.booking.usermanagement.controller;

import com.booking.usermanagement.dtos.TherapyDto;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.service.ServiceImpl.TherapyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user/therapies")
@RequiredArgsConstructor
public class TherapyController {
    private final TherapyService therapyService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('SPONSORADMIN')")
    public Therapy create(@Valid  @RequestBody TherapyDto therapy) {
        return therapyService.createTherapy(therapy);
    }

    @GetMapping("/getAll")
    public List<Therapy> list() {
        return therapyService.getAllTherapies();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SPONSORADMIN')")
    public Therapy update(@PathVariable("id") UUID id, @Valid @RequestBody TherapyDto therapy) {
        return therapyService.updateTherapy(id, therapy);
    }
}
