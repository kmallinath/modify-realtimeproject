package com.booking.usermanagement.service.ServiceImpl;

import com.booking.usermanagement.dtos.TherapyDto;
import com.booking.usermanagement.entities.Therapy;
import com.booking.usermanagement.repository.TherapyRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TherapyService {
    private final TherapyRepository therapyRepository;

    public Therapy createTherapy(TherapyDto therapy) {

        Therapy newTherapy = new Therapy();
        newTherapy.setName(therapy.getName());
        newTherapy.setDescription(therapy.getDescription());
        newTherapy.setPrice(therapy.getPrice());
        return therapyRepository.save(newTherapy);
    }

    public List<Therapy> getAllTherapies() {
        return therapyRepository.findAll();
    }

    public Therapy updateTherapy(UUID id, @Valid TherapyDto therapy) {
        Therapy existingTherapy = therapyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Therapy not found with ID: " + id));

        existingTherapy.setName(therapy.getName());
        existingTherapy.setDescription(therapy.getDescription());
        existingTherapy.setPrice(therapy.getPrice());
        existingTherapy.setActive(therapy.isActive());
        return therapyRepository.save(existingTherapy);
    }
}
