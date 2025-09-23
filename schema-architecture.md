Architecture summary

This Spring Boot application uses both MVC and REST controllers. Thymeleaf templates are used for the Admin and Doctor dashboards, while REST APIs serve all other modules. The application interacts with two databases—MySQL (for patient, doctor, appointment, and admin data) and MongoDB (for prescriptions). All controllers route requests through a common service layer, which in turn delegates to the appropriate repositories. MySQL uses JPA entities while MongoDB uses document models.

Numbered flow of data control

1. User accesses AdminDashboard or Appointment pages.
2. The action is routed to the appropriate Thymeleaf or REST controller.
3. The controller calls the service layer.
4. The Service applies business rules and calls the appropriate repository.
5. The repository reads or writes data in MySQL or MongoDB.
6. The data is mapped to Java models (JPA entities or Mongo documents).
7. The controller returns the response—Thymeleaf renders HTML or REST returns JSON.
