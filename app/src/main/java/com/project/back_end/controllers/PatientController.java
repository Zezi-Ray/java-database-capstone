package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.back_end.models.Patient;
import com.project.back_end.services.AppService;
import com.project.back_end.services.PatientService;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import com.project.back_end.DTO.Login;
import org.springframework.web.bind.annotation.GetMapping;

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller for patient-related operations.
//    - Use `@RequestMapping("/patient")` to prefix all endpoints with `/patient`, grouping all patient functionalities under a common route.ler
@RestController
@RequestMapping("/patient")
public class PatientController {
    // 2. Autowire Dependencies:
    //    - Inject `PatientService` to handle patient-specific logic such as creation, retrieval, and appointments.
    //    - Inject the shared `Service` class for tasks like token validation and login authentication.
    private final PatientService patientService;
    private final AppService service;
    public PatientController(PatientService patientService, AppService service) {
        this.patientService = patientService;   
        this.service = service;
    }
    // 3. Define the `getPatient` Method:
    //    - Handles HTTP GET requests to retrieve patient details using a token.
    //    - Validates the token for the `"patient"` role using the shared service.
    //    - If the token is valid, returns patient information; otherwise, returns an appropriate error message.
    @RequestMapping("/{token}")
    public ResponseEntity<Object> getPatient(String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            Object patient = patientService.getPatientDetails(token);
            return ResponseEntity.ok(patient);
        } else {
            // Convert Map<String, String> to Map<String, Object> for consistent return type
            Map<String, Object> errorBody = new java.util.HashMap<>(tokenValidation.getBody());
            return new ResponseEntity<>(errorBody, tokenValidation.getStatusCode());
        }
    }
    // 4. Define the `createPatient` Method:
    //    - Handles HTTP POST requests for patient registration.
    //    - Accepts a validated `Patient` object in the request body.
    //    - First checks if the patient already exists using the shared service.
    //    - If validation passes, attempts to create the patient and returns success or error messages based on the outcome.
    @RequestMapping("/register")
    public ResponseEntity<Map<String, String>> createPatient(Patient patient) {
        boolean exists = service.validatePatient(patient);
        if (exists) {
            return ResponseEntity.status(409).body(Map.of("error", "Patient already exists"));
        } else {
            int result = patientService.createPatient(patient);
            if (result == 1) {
            return ResponseEntity.ok(Map.of("message", "Patient created successfully"));
            } else {
                return ResponseEntity.status(500).body(Map.of("error", "Failed to create patient"));
            }
        }
    } 
    // 5. Define the `login` Method:
    //    - Handles HTTP POST requests for patient login.
    //    - Accepts a `Login` DTO containing email/username and password.
    //    - Delegates authentication to the `validatePatientLogin` method in the shared service.
    //    - Returns a response with a token or an error message depending on login success.
    @RequestMapping("/login")
    public ResponseEntity<Map<String, Object>> login(Login login) {
        return service.validatePatientLogin(login);
    }
    // 6. Define the `getPatientAppointment` Method:
    //    - Handles HTTP GET requests to fetch appointment details for a specific patient.
    //    - Requires the patient ID, token, and user role as path variables.
    //    - Validates the token using the shared service.
    //    - If valid, retrieves the patient's appointment data from `PatientService`; otherwise, returns a validation error.
    @GetMapping("/appointments/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getPatientAppointments(Long id, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            ResponseEntity<Map<String, Object>> appointments = patientService.getPatientAppointment(id, token);
            return new ResponseEntity<>(appointments.getBody(), appointments.getStatusCode());
        } else {
            // Convert Map<String, String> to Map<String, Object> for consistent return type
            Map<String, Object> errorBody = new java.util.HashMap<>(tokenValidation.getBody());
            return new ResponseEntity<>(errorBody, tokenValidation.getStatusCode());
        }
    }
    // 7. Define the `filterPatientAppointment` Method:
    //    - Handles HTTP GET requests to filter a patient's appointments based on specific conditions.
    //    - Accepts filtering parameters: `condition`, `name`, and a token.
    //    - Token must be valid for a `"patient"` role.
    //    - If valid, delegates filtering logic to the shared service and returns the filtered result.
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            return service.filterPatient(condition, name, token);
        } else {
            // Convert Map<String, String> to Map<String, Object> for consistent return type
            Map<String, Object> errorBody = new java.util.HashMap<>(tokenValidation.getBody());
            return new ResponseEntity<>(errorBody, tokenValidation.getStatusCode());
        }
    }
}


