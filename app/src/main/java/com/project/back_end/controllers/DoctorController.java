package com.project.back_end.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.back_end.services.AppService;
import com.project.back_end.services.DoctorService;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.Map;
import com.project.back_end.models.Doctor;
import com.project.back_end.DTO.Login;

// 1. Set Up the Controller Class:
//    - Annotate the class with `@RestController` to define it as a REST controller that serves JSON responses.
//    - Use `@RequestMapping("${api.path}doctor")` to prefix all endpoints with a configurable API path followed by "doctor".
//    - This class manages doctor-related functionalities such as registration, login, updates, and availability.
@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {
    // 2. Autowire Dependencies:
    //    - Inject `DoctorService` for handling the core logic related to doctors (e.g., CRUD operations, authentication).
    //    - Inject the shared `Service` class for general-purpose features like token validation and filtering.
    private final DoctorService doctorService;
    private final AppService service;
    public DoctorController(DoctorService doctorService, AppService service) {
        this.doctorService = doctorService;
        this.service = service;
    }
    // 3. Define the `getDoctorAvailability` Method:
    //    - Handles HTTP GET requests to check a specific doctor’s availability on a given date.
    //    - Requires `user` type, `doctorId`, `date`, and `token` as path variables.
    //    - First validates the token against the user type.
    //    - If the token is invalid, returns an error response; otherwise, returns the availability status for the doctor.
    @RequestMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<Object> getDoctorAvailability(String user, Long doctorId, LocalDate date, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, user);
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            // doctorService.getDoctorAvailability returns List<String>
            Object availability = doctorService.getDoctorAvailability(doctorId, date);
            return ResponseEntity.ok(availability);
        } else {
            // Convert Map<String, String> to Map<String, Object> for consistent return type
            Map<String, Object> errorBody = new java.util.HashMap<>(tokenValidation.getBody());
            return new ResponseEntity<>(errorBody, tokenValidation.getStatusCode());
        }
    }
    // 4. Define the `getDoctor` Method:
    //    - Handles HTTP GET requests to retrieve a list of all doctors.
    //    - Returns the list within a response map under the key `"doctors"` with HTTP 200 OK status.
    @GetMapping
    public ResponseEntity<Map<String, Object>> getDoctor() {
        return ResponseEntity.ok(Map.of("doctors", doctorService.getDoctors()));
    }
    // 5. Define the `saveDoctor` Method:
    //    - Handles HTTP POST requests to register a new doctor.
    //    - Accepts a validated `Doctor` object in the request body and a token for authorization.
    //    - Validates the token for the `"admin"` role before proceeding.
    //    - If the doctor already exists, returns a conflict response; otherwise, adds the doctor and returns a success message.
    @RequestMapping("/register/{token}")
    public ResponseEntity<Map<String, String>> saveDoctor(Doctor doctor, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            int isSaved = doctorService.saveDoctor(doctor);
            switch (isSaved) {
                case -1:
                    return ResponseEntity.status(409).body(Map.of("error", "Doctor already exists"));
                case 1:
                    return ResponseEntity.ok(Map.of("message", "Doctor registered successfully"));
                default:
                    return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
            }
        } else {
            return tokenValidation;
        }
    }
    // 6. Define the `doctorLogin` Method:
    //    - Handles HTTP POST requests for doctor login.
    //    - Accepts a validated `Login` DTO containing credentials.
    //    - Delegates authentication to the `DoctorService` and returns login status and token information.
    @RequestMapping("/login")
    public ResponseEntity<Map<String, String>> doctorLogin(Login login) {
        return doctorService.validateDoctor(login);
    }
    // 7. Define the `updateDoctor` Method:
    //    - Handles HTTP PUT requests to update an existing doctor's information.
    //    - Accepts a validated `Doctor` object and a token for authorization.
    //    - Token must belong to an `"admin"`.
    //    - If the doctor exists, updates the record and returns success; otherwise, returns not found or error messages.
    @RequestMapping("/update/{token}")
    public ResponseEntity<Map<String, String>> updateDoctor(Doctor doctor, String token) {
        ResponseEntity<Map<String, String>> tokenValidation = service.validateToken(token, "admin");
        if (tokenValidation.getStatusCode().is2xxSuccessful()) {
            int isUpdated = doctorService.updateDoctor(doctor);
            switch (isUpdated) {
                case -1:
                    return ResponseEntity.status(404).body(Map.of("error", "Doctor not found"));
                case 1:
                    return ResponseEntity.ok(Map.of("message", "Doctor updated successfully"));
                default:    
                    return ResponseEntity.status(500).body(Map.of("error", "Internal server error"));
            }
        } else {
            return tokenValidation;
        }
    }
    // 8. Define the `deleteDoctor` Method:
    //    - Handles HTTP DELETE requests to remove a doctor by ID.
    //    - Requires both doctor ID and an admin token as path variables.
    //    - If the doctor exists, deletes the record and returns a success message; otherwise, responds with a not found or error message.
    @RequestMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<Map<String, Object>> filter(String name, String time, String speciality) {
        return ResponseEntity.ok(Map.of("doctors", service.filterDoctor(name, time, speciality)));
    }
    // 9. Define the `filter` Method:
    //    - Handles HTTP GET requests to filter doctors based on name, time, and specialty.
    //    - Accepts `name`, `time`, and `speciality` as path variables.
    //    - Calls the shared `Service` to perform filtering logic and returns matching doctors in the response.


}
