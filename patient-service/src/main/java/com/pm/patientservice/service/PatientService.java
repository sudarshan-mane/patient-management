package com.pm.patientservice.service;

import com.pm.patientservice.dto.PatientRequestDTO;
import com.pm.patientservice.dto.PatientResponseDTO;
import com.pm.patientservice.exception.DuplicateResourceException;
import com.pm.patientservice.exception.ResourceNotFoundException;
import com.pm.patientservice.grpc.BillingServiceGrpcClient;
import com.pm.patientservice.mapper.PatientMapper;
import com.pm.patientservice.model.Patient;
import com.pm.patientservice.repository.PatientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PatientService {
    private static final Logger log = LoggerFactory.getLogger(PatientService.class);
    private PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient) {
        this.patientRepository  = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
    }

    public List<PatientResponseDTO> getPatients(){
        return patientRepository
                .findAll()
                .stream()
                .map(PatientMapper::toDTO)
                .toList();

                //Equivalent
                //.map(patient -> PatientMapper.toDTO(patient)).toList();
    }

     public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO){

        checkEmailAlreadyExist(patientRequestDTO);

        Patient patient = PatientMapper.toEntity(patientRequestDTO);
        Patient newPatient = patientRepository.save(patient);

        log.info("Created patient with id {}",newPatient.getId());
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),
                newPatient.getName(),newPatient.getEmail());
        return PatientMapper.toDTO(newPatient);
     }

     public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO){

         Patient patient = patientRepository.findById(id)
                 .orElseThrow(()-> new ResourceNotFoundException("Patient with id " + id + " not found") );

         checkEmailAlreadyExist(patientRequestDTO, id);

         PatientMapper.updateEntity(patientRequestDTO,patient);

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(updatedPatient);
     }

     public void deletePatient(UUID id){
        patientRepository.deleteById(id);
     }

     private void checkEmailAlreadyExist(PatientRequestDTO patientRequestDTO){
         if(patientRepository.existsByEmail(patientRequestDTO.email())){
             throw new DuplicateResourceException("Patient with Email " + patientRequestDTO.email() +" already exists");
         }
     }

    private void checkEmailAlreadyExist(PatientRequestDTO patientRequestDTO, UUID id){
        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.email(), id)){
            throw new DuplicateResourceException("Patient with Email " + patientRequestDTO.email() +" already exists");
        }
    }

}
