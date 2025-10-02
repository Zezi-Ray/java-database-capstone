package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.back_end.services.AppService;
import com.project.back_end.services.PrescriptionService;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import com.project.back_end.models.Prescription;
// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("${api.path}prescription")` to set the base path for all prescription-related endpoints.
//    - This controller manages creating and retrieving prescriptions tied to appointments.
@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {
    // 2. Autowire Dependencies:
    //    - Inject `PrescriptionService` to handle logic related to saving and fetching prescriptions.
    //    - Inject the shared `Service` class for token validation and role-based access control.
    //    - Inject `AppointmentService` to update appointment status after a prescription is issued.
    private final PrescriptionService prescriptionService;
    private final AppService service;
    public PrescriptionController(PrescriptionService prescriptionService, AppService service) {
        this.prescriptionService = prescriptionService;
        this.service = service;
    }
    // 3. Define the `savePrescription` Method:
    //    - Handles HTTP POST requests to save a new prescription for a given appointment.
    //    - Accepts a validated `Prescription` object in the request body and a doctor’s token as a path variable.
    //    - Validates the token for the `"doctor"` role.
    //    - If the token is valid, updates the status of the corresponding appointment to reflect that a prescription has been added.
    //    - Delegates the saving logic to `PrescriptionService` and returns a response indicating success or failure.
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> savePrescription(Prescription prescription, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            return prescriptionService.savePrescription(prescription);
        } else {
            return tokenValidation;
        }
    }
    // 4. Define the `getPrescription` Method:
    //    - Handles HTTP GET requests to retrieve a prescription by its associated appointment ID.
    //    - Accepts the appointment ID and a doctor’s token as path variables.
    //    - Validates the token for the `"doctor"` role using the shared service.
    //    - If the token is valid, fetches the prescription using the `PrescriptionService`.
    //    - Returns the prescription details or an appropriate error message if validation fails.
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<Object> getPrescription(Long appointmentId, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "doctor");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            Object prescription = prescriptionService.getPrescription(appointmentId);
            return ResponseEntity.ok(prescription);
        } else {                    
            // Convert Map<String, String> to Map<String, Object> for consistent return type
            Map<String, Object> errorBody = new java.util.HashMap<>(tokenValidation.getBody());
            return new ResponseEntity<>(errorBody, tokenValidation.getStatusCode());
        }   
    }
}
