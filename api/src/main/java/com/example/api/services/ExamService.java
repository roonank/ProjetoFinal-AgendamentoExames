package com.example.api.services;

import com.example.api.dtos.exam.ExamCreateUpdateDTO;
import com.example.api.dtos.exam.ExamResponseDTO;
import com.example.api.models.Exam;
import com.example.api.repositories.ExamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    //CREATE EXAME
    public ExamResponseDTO createExam(ExamCreateUpdateDTO dto){
        Exam exam = new Exam();
        copyDtoToEntity(dto, exam);
        exam = examRepository.save(exam);
        return toResponseDTO(exam);
    }

    //UPDATE EXAME
    public ExamResponseDTO updateExam(Long id, ExamCreateUpdateDTO dto){
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exame não encontrado!"));

        copyDtoToEntity(dto, exam);
        exam = examRepository.save(exam);
        return toResponseDTO(exam);
    }

    //DELETE EXAME
    public void deleteExam(Long id){
        if (!examRepository.existsById(id)) {
            throw new IllegalArgumentException("Exame não encontrado!");
        }
        examRepository.deleteById(id);
    }

    public ExamResponseDTO findById(Long id){
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Exame não encontrado!"));
        return toResponseDTO(exam);
    }

    public List<ExamResponseDTO> listAll(){
        return examRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private void copyDtoToEntity(ExamCreateUpdateDTO dto, Exam exam){
        exam.setCode(dto.getCode());
        exam.setName(dto.getName());
        exam.setDescription(dto.getDescription());
        exam.setDeliverTimeInDays(dto.getDeliverTimeInDays());
        exam.setInstructions(dto.getInstructions());
        exam.setPrice(dto.getPrice());
    }

    private ExamResponseDTO toResponseDTO(Exam exam){
        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setId(exam.getId());
        dto.setCode(exam.getCode());
        dto.setName(exam.getName());
        dto.setDescription(exam.getDescription());
        dto.setDeliverTimeInDays(exam.getDeliverTimeInDays());
        dto.setInstructions(exam.getInstructions());
        dto.setPrice(exam.getPrice());
        return dto;
    }
}
