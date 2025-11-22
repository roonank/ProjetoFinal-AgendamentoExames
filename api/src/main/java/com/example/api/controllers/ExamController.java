package com.example.api.controllers;

import com.example.api.dtos.exam.ExamCreateUpdateDTO;
import com.example.api.dtos.exam.ExamResponseDTO;
import com.example.api.services.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamResponseDTO> createExam(@RequestBody @Valid ExamCreateUpdateDTO dto){
        ExamResponseDTO created = examService.createExam(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> updateExam(@PathVariable Long id, @RequestBody @Valid ExamCreateUpdateDTO dto){
        ExamResponseDTO updated = examService.updateExam(id, dto);
        return  ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        examService.deleteExam(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.ok(examService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ExamResponseDTO>> listAll() {
        return ResponseEntity.ok(examService.listAll());
    }

}
