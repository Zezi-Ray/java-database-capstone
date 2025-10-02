package com.project.back_end.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class AppointmentDTO {
    private Long id;
    // 1. 'id' field:
    //    - Type: private Long
    //    - Description:
    //      - Represents the unique identifier for the appointment.
    //      - This is the primary key for identifying the appointment in the system.

    private Long doctorId;
    // 2. 'doctorId' field:
    //    - Type: private Long
    //    - Description:
    //      - Represents the ID of the doctor associated with the appointment.
    //      - This is a simplified field, capturing only the ID of the doctor (not the full Doctor object).

    private String doctorName;
    // 3. 'doctorName' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the name of the doctor associated with the appointment.
    //      - This is a simplified field for displaying the doctor's name.

    private Long patientId;
    // 4. 'patientId' field:
    //    - Type: private Long
    //    - Description:
    //      - Represents the ID of the patient associated with the appointment.
    //      - This is a simplified field, capturing only the ID of the patient (not the full Patient object).

    private String patientName;
    // 5. 'patientName' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the name of the patient associated with the appointment.
    //      - This is a simplified field for displaying the patient's name.

    private String patientEmail;
    // 6. 'patientEmail' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the email of the patient associated with the appointment.
    //      - This is a simplified field for displaying the patient's email.

    private String patientPhone;
    // 7. 'patientPhone' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the phone number of the patient associated with the appointment.
    //      - This is a simplified field for displaying the patient's phone number.

    private String patientAddress;
    // 8. 'patientAddress' field:
    //    - Type: private String
    //    - Description:
    //      - Represents the address of the patient associated with the appointment.
    //      - This is a simplified field for displaying the patient's address.

    private LocalDateTime appointmentTime;
    // 9. 'appointmentTime' field:
    //    - Type: private LocalDateTime
    //    - Description:
    //      - Represents the scheduled date and time of the appointment.
    //      - The time when the appointment is supposed to happen, stored as a LocalDateTime object.

    private int status;
    // 10. 'status' field:
    //    - Type: private int
    //    - Description:
    //      - Represents the status of the appointment.
    //      - Status can indicate if the appointment is "Scheduled:0", "Completed:1", or other statuses (e.g., "Canceled") as needed.

    private LocalDate appointmentDate;
    // 11. 'appointmentDate' field (Custom Getter):
    //    - Type: private LocalDate
    //    - Description:
    //      - A derived field representing only the date part of the appointment (without the time).
    //      - Extracted from the 'appointmentTime' field.

    private LocalTime appointmentTimeOnly;
    // 12. 'appointmentTimeOnly' field (Custom Getter):
    //    - Type: private LocalTime
    //    - Description:
    //      - A derived field representing only the time part of the appointment (without the date).
    //      - Extracted from the 'appointmentTime' field.

    private LocalDateTime endTime;
    // 13. 'endTime' field (Custom Getter):
    //    - Type: private LocalDateTime
    //    - Description:
    //      - A derived field representing the end time of the appointment.
    //      - Calculated by adding 1 hour to the 'appointmentTime' field.

    public AppointmentDTO(Long id, Long doctorId, String doctorName, Long patientId, String patientName,
                          String patientEmail, String patientPhone, String patientAddress,
                          LocalDateTime appointmentTime, int status) {
        this.id = id;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientEmail = patientEmail;
        this.patientPhone = patientPhone;
        this.patientAddress = patientAddress;
        this.appointmentTime = appointmentTime;
        this.status = status;
        this.appointmentDate = appointmentTime.toLocalDate();
        this.appointmentTimeOnly = appointmentTime.toLocalTime();
        this.endTime = appointmentTime.plusHours(1);
    }
    // 14. Constructor:
    //    - The constructor accepts all the relevant fields for the AppointmentDTO, including simplified fields for the doctor and patient (ID, name, etc.).
    //    - It also calculates custom fields: 'appointmentDate', 'appointmentTimeOnly', and 'endTime' based on the 'appointmentTime' field.

    public Long getId() {
        return id;
    }
    public Long getDoctorId() {
        return doctorId;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public Long getPatientId() {
        return patientId;
    }
    public String getPatientName() {
        return patientName;
    }
    public String getPatientEmail() {           
        return patientEmail;
    }
    public String getPatientPhone() {
        return patientPhone;
    }
    public String getPatientAddress() {
        return patientAddress;                                          
    }
    public LocalDateTime getAppointmentTime() {
        return appointmentTime;
    }
    public int getStatus() {
        return status;
    }
    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTimeOnly;
    }
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    // 15. Getters:
    //    - Standard getter methods are provided for all fields: id, doctorId, doctorName, patientId, patientName, patientEmail, patientPhone, patientAddress, appointmentTime, status, appointmentDate, appointmentTimeOnly, and endTime.
    //    - These methods allow access to the values of the fields in the AppointmentDTO object.

}
