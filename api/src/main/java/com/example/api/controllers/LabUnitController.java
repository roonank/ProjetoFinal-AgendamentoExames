package com.example.api.controllers;

import com.example.api.dtos.labUnit.LabUnitCreateUpdateDTO;
import com.example.api.dtos.labUnit.LabUnitResponseDTO;
import com.example.api.services.LabUnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lab-units")
@RequiredArgsConstructor
public class LabUnitController {

    private final LabUnitService labUnitService;

    // Somente ADMIN pode criar
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<LabUnitResponseDTO> create(@RequestBody @Valid LabUnitCreateUpdateDTO dto) {
        LabUnitResponseDTO created = labUnitService.createLabUnit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Somente ADMIN pode atualizar
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<LabUnitResponseDTO> update(@PathVariable Long id,
                                                     @RequestBody @Valid LabUnitCreateUpdateDTO dto) {
        LabUnitResponseDTO updated = labUnitService.updateLabUnit(id, dto);
        return ResponseEntity.ok(updated);
    }

    // Somente ADMIN pode deletar
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        labUnitService.deleteLabUnit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabUnitResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(labUnitService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LabUnitResponseDTO>> listAll() {
        return ResponseEntity.ok(labUnitService.listAll());
    }
}

