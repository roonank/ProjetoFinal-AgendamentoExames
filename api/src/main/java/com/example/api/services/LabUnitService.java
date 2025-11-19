package com.example.api.services;

import com.example.api.dtos.labUnit.LabUnitCreateUpdateDTO;
import com.example.api.dtos.labUnit.LabUnitResponseDTO;
import com.example.api.models.LabUnit;
import com.example.api.repositories.LabUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabUnitService {

    private final LabUnitRepository labUnitRepository;

    //CREATE UNIDADE DE LABORATÓRIO
    public LabUnitResponseDTO createLabUnit(LabUnitCreateUpdateDTO dto){
        LabUnit labUnit = new LabUnit();
        copyDtoToEntity(dto, labUnit);
        labUnit = labUnitRepository.save(labUnit);
        return toResponseDTO(labUnit);
    }

    //UPDATE UNIDADE DE LABORATÓRIO
    public LabUnitResponseDTO updateLabUnit(Long id, LabUnitCreateUpdateDTO dto){
        LabUnit labUnit = labUnitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Clínica não encontrada!"));
        copyDtoToEntity(dto, labUnit);
        labUnit = labUnitRepository.save(labUnit);
        return toResponseDTO(labUnit);
    }

    //DELETE UNIDADE DE LABORATÓRIO
    public void deleteLabUnit(Long id){
        if (!labUnitRepository.existsById(id)) {
            throw new IllegalArgumentException("Unidade não encontrada!");
        }
        labUnitRepository.deleteById(id);
    }

    public LabUnitResponseDTO findById(Long id){
        LabUnit labUnit = labUnitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unidade não encontrada!"));
        return toResponseDTO(labUnit);
    }

    public List<LabUnitResponseDTO> listAll(){
        return labUnitRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    private void copyDtoToEntity(LabUnitCreateUpdateDTO dto, LabUnit labUnit) {
        labUnit.setName(dto.getName());
        labUnit.setAddress(dto.getAddress());
        labUnit.setPhone(dto.getPhone());
        labUnit.setOpeningHours(dto.getOpeningHours());
    }

    private LabUnitResponseDTO toResponseDTO(LabUnit labUnit) {
        LabUnitResponseDTO dto = new LabUnitResponseDTO();
        dto.setId(labUnit.getId());
        dto.setName(labUnit.getName());
        dto.setAddress(labUnit.getAddress());
        dto.setPhone(labUnit.getPhone());
        dto.setOpeningHours(labUnit.getOpeningHours());
        return dto;
    }
}