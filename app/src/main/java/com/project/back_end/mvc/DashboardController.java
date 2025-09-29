package com.project.back_end.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// 1. Set Up the MVC Controller Class:
//    - Annotate the class with `@Controller` to indicate that it serves as an MVC controller returning view names (not JSON).
//    - This class handles routing to admin and doctor dashboard pages based on token validation.
@Controller
public class DashboardController {

    // Define the `adminDashboard` Method:
    @GetMapping("/adminDashboard/{token}")
    // Handle GET requests to /adminDashboard/{token}
    public String adminDashboard (@PathVariable String token) {
    // Accept the token as a path variable
        String error = new ValidationToken().validateToken(token, "admin");
        // Validate the token for the "admin" role
        if (error.isEmpty()) {
            return "admin/adminDashboard"; // Forward to admin dashboard view
        } 
        // If the token is valid (i.e., no errors returned), forwards the user to the "admin/adminDashboard" view.
        else {
            return "redirect:/"; // Redirect to root URL on invalid token
        }
        // If invalid, redirects to the root URL, likely the login or home page.
    }

    // Define the `doctorDashboard` Method:
    @GetMapping("/doctorDashboard/{token}")
    // Handle GET requests to /doctorDashboard/{token}
    public String doctorDashboard (@PathVariable String token) {
    // Accept the token as a path variable
        String error = new ValidationToken().validateToken(token, "doctor");
        // Validate the token for the "doctor" role
        if (error.isEmpty()) {
            return "doctor/doctorDashboard"; // Forward to doctor dashboard view
        } 
        // If the token is valid (i.e., no errors returned), forwards the user to the "doctor/doctorDashboard" view.
        else {
            return "redirect:/"; // Redirect to root URL on invalid token
        }
        // If invalid, redirects to the root URL, redirects to the root URL
    }

}

// Autowire the shared service
@Service
class ValidationToken {
    // Inject the common Service class, which provides the token validation logic used to authorize access to dashboards
    public String validateToken(String token, String role) {
        if ("validAdminToken".equals(token) && "admin".equals(role)) {
            return "";
        } else if ("validDoctorToken".equals(token) && "doctor".equals(role)) {
            return "";
        }
        return "Invalid token";
    }
}
