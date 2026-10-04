package com.pm.patientservice.controller;

import com.pm.patientservice.dto.CreatePatientValidationGroup;
import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/patients")
@Tag(name="Patient", description = "API for Patients management")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping
    @Operation(summary = "Create Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(@Validated({Default.class, CreatePatientValidationGroup.class}) @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patient = patientService.createPatient(patientRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @GetMapping
    @Operation(summary = "Get Patient")
    public ResponseEntity<List<PatientResponseDTO>> getPatients(){
        List<PatientResponseDTO> patients = patientService.getPatients();
        return ResponseEntity.status(HttpStatus.OK).body(patients);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @Valid @RequestBody PatientRequestDTO patientRequestDTO) {
        PatientResponseDTO patient = patientService.updatePatient(id, patientRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(patient);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Patient")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
