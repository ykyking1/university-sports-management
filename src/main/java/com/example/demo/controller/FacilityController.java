package com.example.demo.controller;

import com.example.demo.dto.FacilityDTO;
import com.example.demo.entity.Facility;
import com.example.demo.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    @GetMapping
    public ResponseEntity<List<FacilityDTO>> getAll() {
        List<FacilityDTO> dtos = facilityService.getAll()
                .stream()
                .map(facilityService::mapToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacilityDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.mapToDTO(facilityService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<FacilityDTO> create(@RequestBody Facility facility) {
        return new ResponseEntity<>(
                facilityService.mapToDTO(facilityService.create(facility)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FacilityDTO> update(
            @PathVariable Long id,
            @RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.mapToDTO(facilityService.update(id, facility)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}