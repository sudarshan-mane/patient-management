package com.pm.patientservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
//Note: Records automatically accessor methods; no Lombok required.
public record PatientRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 character")
        String name,

        @NotBlank(message = "Email is required")
        @Email
        String email,

        @NotBlank(message = "Address is required")
        String address,

        @NotNull(message = "Date of Birth is required")
        LocalDate dateOfBirth,

        @NotNull(groups = CreatePatientValidationGroup.class, message = "Date of Registration is required")
        LocalDate registeredDate
){}
