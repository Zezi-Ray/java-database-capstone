package com.project.back_end.services;

import org.springframework.stereotype.Service;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.DTO.Login;
import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;

import jakarta.transaction.Transactional;

import com.project.back_end.repo.AppointmentRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;

// 1. **@Service Annotation**
// The @Service annotation marks this class as a service component in Spring. This allows Spring to automatically detect it through component scanning
// and manage its lifecycle, enabling it to be injected into controllers or other services using @Autowired or constructor injection.
@Service
public class AppService {
    // 2. **Constructor Injection for Dependencies**
    // The constructor injects all required dependencies (TokenService, Repositories, and other Services). This approach promotes loose coupling, improves testability,
    // and ensures that all required dependencies are provided at object creation time.
    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;
    public AppService(TokenService tokenService, AdminRepository adminRepository, DoctorRepository doctorRepository, PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorService doctorService, PatientService patientService) {
        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    // 3. **validateToken Method**
    // This method checks if the provided JWT token is valid for a specific user. It uses the TokenService to perform the validation.
    // If the token is invalid or expired, it returns a 401 Unauthorized response with an appropriate error message. This ensures security by preventing
    // unauthorized access to protected resources.
    @Transactional
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        boolean response = tokenService.validateToken(token, user);
        if (!response) {    
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
        }
        return ResponseEntity.ok(Map.of("message", "Token is valid"));
    }
    // 4. **validateAdmin Method**
    // This method validates the login credentials for an admin user.
    // - It first searches the admin repository using the provided username.
    // - If an admin is found, it checks if the password matches.
    // - If the password is correct, it generates and returns a JWT token (using the admin’s username) with a 200 OK status.
    // - If the password is incorrect, it returns a 401 Unauthorized status with an error message.
    // - If no admin is found, it also returns a 401 Unauthorized.
    // - If any unexpected error occurs during the process, a 500 Internal Server Error response is returned.
    // This method ensures that only valid admin users can access secured parts of the system.
    @Transactional
    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());
            if (admin != null) {
                if (admin.getPassword().equals(receivedAdmin.getPassword())) {
                    String token = tokenService.generateToken(admin.getUsername());
                    return ResponseEntity.ok(Map.of("token", token));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Admin not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while processing the request"));
        }
    }
    // 5. **filterDoctor Method**
    // This method provides filtering functionality for doctors based on name, specialty, and available time slots.
    // - It supports various combinations of the three filters.
    // - If none of the filters are provided, it returns all available doctors.
    // This flexible filtering mechanism allows the frontend or consumers of the API to search and narrow down doctors based on user criteria.
    @Transactional
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        try {
            if (name != null && specialty != null && time != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorsByNameSpecialtyAndTime(name, specialty, time)));
            } else if (name != null && specialty != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorByNameAndSpecility(name, specialty)));
            } else if (name != null && time != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorByNameAndTime(name, time)));
            } else if (specialty != null && time != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorByTimeAndSpecility(specialty, time)));
            } else if (name != null) {
                return Map.of("doctors", extractDoctors(doctorService.findDoctorByName(name)));
            } else if (specialty != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorsBySpecility(specialty)));
            } else if (time != null) {
                return Map.of("doctors", extractDoctors(doctorService.filterDoctorsByTime(time)));
            } else {
                return Map.of("doctors", doctorService.getDoctors());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("error", "An error occurred while filtering doctors");
        }
    }

    @SuppressWarnings("unchecked")
    private java.util.List<Doctor> extractDoctors(Object result) {
        if (result instanceof java.util.Map<?, ?> map) {
            Object doctors = map.get("doctors");
            if (doctors instanceof java.util.List<?> list) {
                return (java.util.List<Doctor>) list;
            }
        } else if (result instanceof java.util.List<?> list) {
            return (java.util.List<Doctor>) list;
        }
        return java.util.List.of();
    }
    // 6. **validateAppointment Method**
    // This method validates if the requested appointment time for a doctor is available.
    // - It first checks if the doctor exists in the repository.
    // - Then, it retrieves the list of available time slots for the doctor on the specified date.
    // - It compares the requested appointment time with the start times of these slots.
    // - If a match is found, it returns 1 (valid appointment time).
    // - If no matching time slot is found, it returns 0 (invalid).
    // - If the doctor doesn’t exist, it returns -1.
    // This logic prevents overlapping or invalid appointment bookings.
    @Transactional
    public int validateAppointment(Appointment appointment) {
        try {
            var doctor = doctorRepository.findById(appointment.getDoctor().getId()).orElse(null);
            if (doctor == null) {
                return -1; // Doctor does not exist
            } else {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                String requestedStart = appointment.getAppointmentTime().toLocalTime().format(formatter);

                List<String> configuredSlots = doctor.getAvailableTimes() == null
                        ? Collections.emptyList()
                        : doctor.getAvailableTimes();
                boolean slotConfigured = configuredSlots.stream()
                        .anyMatch(slot -> slot.startsWith(requestedStart));
                if (!slotConfigured) {
                    return 0;
                }

                LocalDateTime start = appointment.getAppointmentTime().withSecond(0).withNano(0);
                LocalDateTime end = start.plusMinutes(1);
                List<Appointment> overlapping = appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(doctor.getId(), start, end);

                boolean hasConflict = overlapping.stream()
                        .anyMatch(existing -> appointment.getId() == null
                                || !existing.getId().equals(appointment.getId()));
                return hasConflict ? 0 : 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0; // Invalid appointment time
        }
    }
    // 7. **validatePatient Method**
    // This method checks whether a patient with the same email or phone number already exists in the system.
    // - If a match is found, it returns false (indicating the patient is not valid for new registration).
    // - If no match is found, it returns true.
    // This helps enforce uniqueness constraints on patient records and prevent duplicate entries.
    @Transactional
    public boolean validatePatient(Patient patient) {
        try {
            var existingPatientByEmail = patientRepository.findByEmail(patient.getEmail());
            var existingPatientByPhone = patientRepository.findByPhone(patient.getPhone());
            if (existingPatientByEmail != null || existingPatientByPhone != null) {
                return false; // Patient with same email or phone number exists
            }
            return true; // Patient is valid for registration
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Assume invalid in case of error
        }
    }
    // 8. **validatePatientLogin Method**
    // This method handles login validation for patient users.
    // - It looks up the patient by email.
    // - If found, it checks whether the provided password matches the stored one.
    // - On successful validation, it generates a JWT token and returns it with a 200 OK status.
    // - If the password is incorrect or the patient doesn't exist, it returns a 401 Unauthorized with a relevant error.
    // - If an exception occurs, it returns a 500 Internal Server Error.
    // This method ensures only legitimate patients can log in and access their data securely.
    @Transactional
    public ResponseEntity<Map<String, Object>> validatePatientLogin(Login login) {
        try {
            var patient = patientRepository.findByEmail(login.getEmail());
            if (patient != null) {
                if (patient.getPassword().equals(login.getPassword())) {
                    String token = tokenService.generateToken(patient.getEmail());
                    return ResponseEntity.ok(Map.of("token", token, "patient", patient));
                } else {
                    return ResponseEntity.status(401).body(Map.of("error", "Invalid password"));
                }
            } else {
                return ResponseEntity.status(401).body(Map.of("error", "Patient not found"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "An error occurred while processing the request"));
        }
    }
    // 9. **filterPatient Method**
    // This method filters a patient's appointment history based on condition and doctor name.
    // - It extracts the email from the JWT token to identify the patient.
    // - Depending on which filters (condition, doctor name) are provided, it delegates the filtering logic to PatientService.
    // - If no filters are provided, it retrieves all appointments for the patient.
    // This flexible method supports patient-specific querying and enhances user experience on the client side.
    @Transactional
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        try {
            String email = tokenService.extractEmail(token);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired token"));
            }
            var patient = patientRepository.findByEmail(email);
            if (patient == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Patient not found"));
            }
            Long patientId = patient.getId();
            if (condition != null && name != null) {
                return patientService.filterByDoctorAndCondition(name, condition, patientId);
            } else if (name != null) {
                return patientService.filterByDoctor(name, patientId);
            } else if (condition != null) {
                return patientService.filterByCondition(condition, patientId);
            } else {
                var appointments = appointmentRepository.findByPatientId(patientId);
                var appointmentDTOs = appointments.stream().map(this::toDto).toList();
                return ResponseEntity.ok(Map.of("appointments", appointmentDTOs));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
        }
    }

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
}
