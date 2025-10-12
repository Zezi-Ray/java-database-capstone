package com.project.back_end.services;

import org.springframework.stereotype.Service;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.models.Appointment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.time.LocalDate;
import java.util.List;

// 1. **Add @Service Annotation**:
//    - To indicate that this class is a service layer class for handling business logic.
//    - The `@Service` annotation should be added before the class declaration to mark it as a Spring service component.
@Service
public class AppointmentService {
    // 2. **Constructor Injection for Dependencies**:
    //    - The `AppointmentService` class requires several dependencies like `AppointmentRepository`, `Service`, `TokenService`, `PatientRepository`, and `DoctorRepository`.
    //    - These dependencies should be injected through the constructor.
    //    - Instruction: Ensure constructor injection is used for proper dependency management in Spring.
    private final AppointmentRepository appointmentRepository;
    private final AppService appService;
    private final TokenService tokenService;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    public AppointmentService(AppointmentRepository appointmentRepository, AppService appService, TokenService tokenService, PatientRepository patientRepository, DoctorRepository doctorRepository) {
        this.appointmentRepository = appointmentRepository;
        this.appService = appService;
        this.tokenService = tokenService;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
    }
    // 3. **Add @Transactional Annotation for Methods that Modify Database**:
    //    - The methods that modify or update the database should be annotated with `@Transactional` to ensure atomicity and consistency of the operations.
    //    - Instruction: Add the `@Transactional` annotation above methods that interact with the database, especially those modifying data.
    @Transactional
    public int bookAppointment(Appointment appointment) {
        try {
            appointmentRepository.save(appointment);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
    // 4. **Book Appointment Method**:
    //    - Responsible for saving the new appointment to the database.
    //    - If the save operation fails, it returns `0`; otherwise, it returns `1`.
    //    - Instruction: Ensure that the method handles any exceptions and returns an appropriate result code.
    @Transactional
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment) {
        try {
            Appointment existingAppointment = appointmentRepository.findById(appointment.getId()).orElse(null);
            if (existingAppointment == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Appointment not found"));
            }
            existingAppointment.setAppointmentTime(appointment.getAppointmentTime());
            existingAppointment.setStatus(appointment.getStatus());
            int isValid = appService.validateAppointment(existingAppointment);
            if (isValid != 1) {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid appointment time"));
            }
            appointmentRepository.save(existingAppointment);
            return ResponseEntity.ok(Map.of("message", "Appointment updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error updating appointment"));
        }
    }
    // 5. **Update Appointment Method**:
    //    - This method is used to update an existing appointment based on its ID.
    //    - It validates whether the patient ID matches, checks if the appointment is available for updating, and ensures that the doctor is available at the specified time.
    //    - If the update is successful, it saves the appointment; otherwise, it returns an appropriate error message.
    //    - Instruction: Ensure proper validation and error handling is included for appointment updates.
    @Transactional
    public ResponseEntity<Map<String, String>> cancelAppointment(Long id, String token) {
        try {
            String patientEmail = tokenService.extractEmail(token);
            Long patientId = patientRepository.findByEmail(patientEmail).getId();
            Appointment appointment = appointmentRepository.findById(id).orElse(null);
            if (appointment == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Appointment not found"));
            }
            if (!appointment.getPatient().getId().equals(patientId)) {
                return ResponseEntity.status(403).body(Map.of("error", "Unauthorized to cancel this appointment"));
            }
            appointmentRepository.delete(appointment);
            return ResponseEntity.ok(Map.of("message", "Appointment cancelled successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error cancelling appointment"));
        }
    }
    // 6. **Cancel Appointment Method**:
    //    - This method cancels an appointment by deleting it from the database.
    //    - It ensures the patient who owns the appointment is trying to cancel it and handles possible errors.
    //    - Instruction: Make sure that the method checks for the patient ID match before deleting the appointment.
    @Transactional
    public Map<String, Object> getAppointments(String patientName, LocalDate date, String token ) {
        List <Appointment> appointments;
        String doctorEmail = tokenService.extractEmail(token);
        Long doctorId = doctorRepository.findByEmail(doctorEmail).getId();
        if (date == null) {
        appointments = (patientName == null || patientName.isBlank())
            ? appointmentRepository.findByDoctorId(doctorId)
            : appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCase(doctorId, patientName);
        } else if (patientName == null || patientName.isBlank()) {
            appointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                    doctorId, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        } else {
            appointments = appointmentRepository.findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                    doctorId, patientName, date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        }
        return Map.of("appointments", appointments);
    }
    // 7. **Get Appointments Method**:
    //    - This method retrieves a list of appointments for a specific doctor on a particular day, optionally filtered by the patient's name.
    //    - It uses `@Transactional` to ensure that database operations are consistent and handled in a single transaction.
    //    - Instruction: Ensure the correct use of transaction boundaries, especially when querying the database for appointments.

    // 8. **Change Status Method**:
    //    - This method updates the status of an appointment by changing its value in the database.
    //    - It should be annotated with `@Transactional` to ensure the operation is executed in a single transaction.
    //    - Instruction: Add `@Transactional` before this method to ensure atomicity when updating appointment status.


}
