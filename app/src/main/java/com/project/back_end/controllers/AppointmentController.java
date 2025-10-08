package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.back_end.services.AppService;
import com.project.back_end.services.AppointmentService;
import org.springframework.http.ResponseEntity;
import com.project.back_end.models.Appointment;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST API controller.
//    - Use `@RequestMapping("/appointments")` to set a base path for all appointment-related endpoints.
//    - This centralizes all routes that deal with booking, updating, retrieving, and canceling appointments.
@RestController
@RequestMapping("/appointments")
public class AppointmentController {
    // 2. Autowire Dependencies:
    //    - Inject `AppointmentService` for handling the business logic specific to appointments.
    //    - Inject the general `Service` class, which provides shared functionality like token validation and appointment checks.
    private final AppointmentService appointmentService;
    private final AppService service;
    public AppointmentController(AppointmentService appointmentService, AppService service) {
        this.appointmentService = appointmentService;
        this.service = service;
    }
    // 3. Define the `getAppointments` Method:
    //    - Handles HTTP GET requests to fetch appointments based on date and patient name.
    //    - Takes the appointment date, patient name, and token as path variables.
    //    - First validates the token for role `"doctor"` using the `Service`.
    //    - If the token is valid, returns appointments for the given patient on the specified date.
    //    - If the token is invalid or expired, responds with the appropriate message and status code.
    @GetMapping("/{patientName}/{date}/{token:.+}")
    public ResponseEntity<Map<String, Object>> getAppointments(@PathVariable("patientName") String patientName, @PathVariable("date") String dateSegment, @PathVariable("token") String token) {
        String normalizedName = "all".equalsIgnoreCase(patientName) ? null : patientName;
        LocalDate date = "all".equalsIgnoreCase(dateSegment) ? null : LocalDate.parse(dateSegment);
        ResponseEntity<Map<String,String>> check = service.validateToken(token,"doctor");
        if (!check.getStatusCode().is2xxSuccessful()) {
            return new ResponseEntity<>(new HashMap<>(check.getBody()), check.getStatusCode());
        }

        Map<String,Object> results = appointmentService.getAppointments(normalizedName, date, token);
        return ResponseEntity.ok(results);

    }
    // 4. Define the `bookAppointment` Method:
    //    - Handles HTTP POST requests to create a new appointment.
    //    - Accepts a validated `Appointment` object in the request body and a token as a path variable.
    //    - Validates the token for the `"patient"` role.
    //    - Uses service logic to validate the appointment data (e.g., check for doctor availability and time conflicts).
    //    - Returns success if booked, or appropriate error messages if the doctor ID is invalid or the slot is already taken.
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> bookAppointment(Appointment appointment, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            int isValid = service.validateAppointment(appointment);
            if (isValid == 0) {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid doctor ID"));
            } else if (isValid == 2) {
                return ResponseEntity.status(409).body(Map.of("error", "Appointment slot already taken"));
            } else {
                int result = appointmentService.bookAppointment(appointment);
                if (result == 1) {
                    return ResponseEntity.ok(Map.of("message", "Appointment booked successfully"));
                } else {
                    return ResponseEntity.status(500).body(Map.of("error", "Failed to book appointment"));
                }
            }
        } else {
            return tokenValidation;
        }
    }
    // 5. Define the `updateAppointment` Method:
    //    - Handles HTTP PUT requests to modify an existing appointment.
    //    - Accepts a validated `Appointment` object and a token as input.
    //    - Validates the token for `"patient"` role.
    //    - Delegates the update logic to the `AppointmentService`.
    //    - Returns an appropriate success or failure response based on the update result.
    @RequestMapping("/update/{token}")
    public ResponseEntity<Map<String, String>> updateAppointment(Appointment appointment, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            return appointmentService.updateAppointment(appointment);
        } else {
            return tokenValidation;
        }
    }
    // 6. Define the `cancelAppointment` Method:
    //    - Handles HTTP DELETE requests to cancel a specific appointment.
    //    - Accepts the appointment ID and a token as path variables.
    //    - Validates the token for `"patient"` role to ensure the user is authorized to cancel the appointment.
    //    - Calls `AppointmentService` to handle the cancellation process and returns the result.
    @RequestMapping("/cancel/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancelAppointment(Long id, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "patient");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            return appointmentService.cancelAppointment(id, token);
        } else {
            return tokenValidation;
        }
    }
}