package com.example.demo.service;

import com.example.demo.entity.Facility;
import com.example.demo.repository.FacilityRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacilityService {

    private final FacilityRepository facilityRepository;

    public List<Facility> getAll() {
        return facilityRepository.findAll();
    }

    public Facility getById(Long id) {
        return facilityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tesis bulunamadı, id: " + id));
    }

    public Facility create(Facility facility) {
        return facilityRepository.save(facility);
    }

    public Facility update(Long id, Facility updated) {
        Facility existing = getById(id);
        existing.setName(updated.getName());
        existing.setType(updated.getType());
        existing.setCapacity(updated.getCapacity());
        existing.setHourlyRate(updated.getHourlyRate());
        existing.setOperatingHours(updated.getOperatingHours());
        return facilityRepository.save(existing);
    }

    public void delete(Long id) {
        Facility existing = getById(id);
        facilityRepository.delete(existing);
    }
}
