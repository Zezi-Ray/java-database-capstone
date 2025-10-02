package com.project.back_end.services;

import org.springframework.stereotype.Service;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;

import jakarta.transaction.Transactional;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import java.util.List;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.DTO.AppointmentDTO;

// 1. **Add @Service Annotation**:
//    - The `@Service` annotation is used to mark this class as a Spring service component. 
//    - It will be managed by Spring's container and used for business logic related to patients and appointments.
//    - Instruction: Ensure that the `@Service` annotation is applied above the class declaration.
@Service
public class PatientService {
// 2. **Constructor Injection for Dependencies**:
//    - The `PatientService` class has dependencies on `PatientRepository`, `AppointmentRepository`, and `TokenService`.
//    - These dependencies are injected via the constructor to maintain good practices of dependency injection and testing.
//    - Instruction: Ensure constructor injection is used for all the required dependencies.
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final TokenService tokenService;

    public PatientService(PatientRepository patientRepository, AppointmentRepository appointmentRepository, TokenService tokenService) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;
    }

    @Transactional
    public int createPatient(Patient patient) {
        try {
            patientRepository.save(patient);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // 3. **createPatient Method**:
    //    - Creates a new patient in the database. It saves the patient object using the `PatientRepository`.
    //    - If the patient is successfully saved, the method returns `1`; otherwise, it logs the error and returns `0`.
    //    - Instruction: Ensure that error handling is done properly and exceptions are caught and logged appropriately.
    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientAppointment(Long id, String token) {
        try {
            String email = tokenService.extractEmail(token);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null || !patient.getId().equals(id)) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized access to patient data"));
            }
            List<Appointment> appointments = appointmentRepository.findByPatientId(id);
            List<AppointmentDTO> appointmentDTOs = appointments.stream().map(this::toDto).toList(); 
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    // 4. **getPatientAppointment Method**:
    //    - Retrieves a list of appointments for a specific patient, based on their ID.
    //    - The appointments are then converted into `AppointmentDTO` objects for easier consumption by the API client.
    //    - This method is marked as `@Transactional` to ensure database consistency during the transaction.
    //    - Instruction: Ensure that appointment data is properly converted into DTOs and the method handles errors gracefully.
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByCondition(String condition, Long id) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0; // Future appointments
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1; // Past appointments
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid condition. Use 'past' or 'future'."));
            }
            List<Appointment> appointments = appointmentRepository.findByPatient_IdAndStatusOrderByAppointmentTimeAsc(id, status);
            List<AppointmentDTO> appointmentDTOs = appointments.stream().map(this::toDto).toList(); 
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    // 5. **filterByCondition Method**:
    //    - Filters appointments for a patient based on the condition (e.g., "past" or "future").
    //    - Retrieves appointments with a specific status (0 for future, 1 for past) for the patient.
    //    - Converts the appointments into `AppointmentDTO` and returns them in the response.
    //    - Instruction: Ensure the method correctly handles "past" and "future" conditions, and that invalid conditions are caught and returned as errors.
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctor(String doctorName, Long patientId) {
        try {
            List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientId(doctorName, patientId);
            List<AppointmentDTO> appointmentDTOs = appointments.stream().map(this::toDto).toList(); 
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    // 6. **filterByDoctor Method**:
    //    - Filters appointments for a patient based on the doctor's name.
    //    - It retrieves appointments where the doctor’s name matches the given value, and the patient ID matches the provided ID.
    //    - Instruction: Ensure that the method correctly filters by doctor's name and patient ID and handles any errors or invalid cases.
    @Transactional
    public ResponseEntity<Map<String, Object>> filterByDoctorAndCondition(String doctorName, String condition, Long patientId) {
        try {
            int status;
            if (condition.equalsIgnoreCase("future")) {
                status = 0; // Future appointments
            } else if (condition.equalsIgnoreCase("past")) {
                status = 1; // Past appointments
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid condition. Use 'past' or 'future'."));
            }
            List<Appointment> appointments = appointmentRepository.filterByDoctorNameAndPatientIdAndStatus(doctorName, patientId, status);
            List<AppointmentDTO> appointmentDTOs = appointments.stream().map(this::toDto).toList(); 
            return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    // 7. **filterByDoctorAndCondition Method**:
    //    - Filters appointments based on both the doctor's name and the condition (past or future) for a specific patient.
    //    - This method combines filtering by doctor name and appointment status (past or future).
    //    - Converts the appointments into `AppointmentDTO` objects and returns them in the response.
    //    - Instruction: Ensure that the filter handles both doctor name and condition properly, and catches errors for invalid input.
    @Transactional
    public ResponseEntity<Map<String, Object>> getPatientDetails(String token) {
        try {
            String email = tokenService.extractEmail(token);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }
            Patient patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Patient not found"));
            }
            return ResponseEntity.ok(Map.of("patient", patient));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }
    // 8. **getPatientDetails Method**:
    //    - Retrieves patient details using the `tokenService` to extract the patient's email from the provided token.
    //    - Once the email is extracted, it fetches the corresponding patient from the `patientRepository`.
    //    - It returns the patient's information in the response body.
    //    - Instruction: Make sure that the token extraction process works correctly and patient details are fetched properly based on the extracted email.

    // 9. **Handling Exceptions and Errors**:
    //    - The service methods handle exceptions using try-catch blocks and log any issues that occur. If an error occurs during database operations, the service responds with appropriate HTTP status codes (e.g., `500 Internal Server Error`).
    //    - Instruction: Ensure that error handling is consistent across the service, with proper logging and meaningful error messages returned to the client.
    private AppointmentDTO toDto(Appointment a) {
        if (a == null) return null;
        Doctor d = a.getDoctor();
        Patient p = a.getPatient();
        return new AppointmentDTO(
            a.getId(),
            d != null ? d.getId() : null,
            d != null ? d.getName() : null,
            p != null ? p.getId() : null,
            p != null ? p.getName() : null,
            p != null ? p.getEmail() : null,
            p != null ? p.getPhone() : null,
            p != null ? p.getAddress() : null,
            a.getAppointmentTime(),
            a.getStatus()
        );
    }
    // 10. **Use of DTOs (Data Transfer Objects)**:
    //    - The service uses `AppointmentDTO` to transfer appointment-related data between layers. This ensures that sensitive or unnecessary data (e.g., password or private patient information) is not exposed in the response.
    //    - Instruction: Ensure that DTOs are used appropriately to limit the exposure of internal data and only send the relevant fields to the client.

}
