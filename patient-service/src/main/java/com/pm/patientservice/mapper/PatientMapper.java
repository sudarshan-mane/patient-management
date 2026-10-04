package com.pm.patientservice.mapper;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.model.Patient;

public class PatientMapper {

    public static PatientResponseDTO toDTO(Patient patient){

        return new PatientResponseDTO(
                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getDateOfBirth()
        );
    }

    public static Patient toEntity(PatientRequestDTO patientRequestDTO){
        Patient patient = new Patient();

        patient.setName(patientRequestDTO.name());
        patient.setEmail(patientRequestDTO.email());
        patient.setAddress(patientRequestDTO.address());
        patient.setDateOfBirth(patientRequestDTO.dateOfBirth());
        patient.setRegisteredDate(patientRequestDTO.registeredDate());

        return patient;
    }

    public static void updateEntity(
            PatientRequestDTO dto,
            Patient patient
    ) {
        patient.setName(dto.name());
        patient.setEmail(dto.email());
        patient.setAddress(dto.address());
        patient.setDateOfBirth(dto.dateOfBirth());

        //IMP: registeredDate is optional so have to check, otherwise will be overridden by null.
        if (dto.registeredDate() != null) {
            patient.setRegisteredDate(dto.registeredDate());
        }
    }
}
