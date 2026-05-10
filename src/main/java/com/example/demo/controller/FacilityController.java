package com.example.demo.controller;

import com.example.demo.entity.Facility;
import com.example.demo.service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facilities")
@RequiredArgsConstructor
public class FacilityController {

    private final FacilityService facilityService;

    /**
     * GET /api/facilities
     * Tüm tesisleri listeler.
     */
    @GetMapping
    public ResponseEntity<List<Facility>> getAll() {
        return ResponseEntity.ok(facilityService.getAll());
    }

    /**
     * GET /api/facilities/{id}
     * Tek bir tesis getirir.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Facility> getById(@PathVariable Long id) {
        return ResponseEntity.ok(facilityService.getById(id));
    }

    /**
     * POST /api/facilities
     * Yeni tesis oluşturur. (Admin yetkisi gerekir — Kişi 1'in Security config'i bunu kontrol eder)
     * Örnek body: { "name": "Spor Salonu A", "type": "Kapalı", "capacity": 500, "hourlyRate": 150.0, "operatingHours": "08:00-22:00" }
     */
    @PostMapping
    public ResponseEntity<Facility> create(@RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.create(facility));
    }

    /**
     * PUT /api/facilities/{id}
     * Mevcut tesisi günceller.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Facility> update(
            @PathVariable Long id,
            @RequestBody Facility facility) {
        return ResponseEntity.ok(facilityService.update(id, facility));
    }

    /**
     * DELETE /api/facilities/{id}
     * Tesis siler.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        facilityService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
