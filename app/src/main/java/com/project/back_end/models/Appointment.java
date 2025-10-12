package com.project.back_end.models;

import java.beans.Transient;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.GenerationType;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
// @Entity annotation:
//    - Marks the class as a JPA entity, meaning it represents a table in the database.
//    - Required for persistence frameworks (e.g., Hibernate) to map the class to a database table.
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  // 1. 'id' field:
  //    - Type: private Long
  //    - Description:
  //      - Represents the unique identifier for each appointment.
  //      - The @Id annotation marks it as the primary key.
  //      - The @GeneratedValue(strategy = GenerationType.IDENTITY) annotation auto-generates the ID value when a new record is inserted into the database.

  @ManyToOne
  @NotNull
  private Doctor doctor;
  // 2. 'doctor' field:
  //    - Type: private Doctor
  //    - Description:
  //      - Represents the doctor assigned to this appointment.
  //      - The @ManyToOne annotation defines the relationship, indicating many appointments can be linked to one doctor.
  //      - The @NotNull annotation ensures that an appointment must be associated with a doctor when created.

  @ManyToOne
  @NotNull
  private Patient patient;
  // 3. 'patient' field:
  //    - Type: private Patient
  //    - Description:
  //      - Represents the patient assigned to this appointment.
  //      - The @ManyToOne annotation defines the relationship, indicating many appointments can be linked to one patient.
  //      - The @NotNull annotation ensures that an appointment must be associated with a patient when created.

  @Future
  private LocalDateTime appointmentTime;
  // 4. 'appointmentTime' field:
  //    - Type: private LocalDateTime
  //    - Description:
  //      - Represents the date and time when the appointment is scheduled to occur.
  //      - The @Future annotation ensures that the appointment time is always in the future when the appointment is created.
  //      - It uses LocalDateTime, which includes both the date and time for the appointment.

  @NotNull
  private int status;
  // 5. 'status' field:
  //    - Type: private int
  //    - Description:
  //      - Represents the current status of the appointment. It is an integer where:
  //        - 0 means the appointment is scheduled.
  //        - 1 means the appointment has been completed.
  //        - 2 means the patient did not show up.
  //      - The @NotNull annotation ensures that the status field is not null.

  @Transient
  public LocalDateTime getEndTime() {
      return appointmentTime.plusHours(1);
  }
  // 6. 'getEndTime' method:
  //    - Type: private LocalDateTime
  //    - Description:
  //      - This method is a transient field (not persisted in the database).
  //      - It calculates the end time of the appointment by adding one hour to the start time (appointmentTime).
  //      - It is used to get an estimated appointment end time for display purposes.

  @Transient
    public LocalDate getAppointmentDate() {
        return appointmentTime.toLocalDate();
    }
  // 7. 'getAppointmentDate' method:
  //    - Type: private LocalDate
  //    - Description:
  //      - This method extracts only the date part from the appointmentTime field.
  //      - It returns a LocalDate object representing just the date (without the time) of the scheduled appointment.

  @Transient
    public LocalTime getAppointmentTimeOnly() {
        return appointmentTime.toLocalTime();
    }
  // 8. 'getAppointmentTimeOnly' method:
  //    - Type: private LocalTime
  //    - Description:
  //      - This method extracts only the time part from the appointmentTime field.
  //      - It returns a LocalTime object representing just the time (without the date) of the scheduled appointment.

  // 9. Constructor(s):
  //    - A no-argument constructor is implicitly provided by JPA for entity creation.
  //    - A parameterized constructor can be added as needed to initialize fields.

  public Long getId() {
      return id;
  } 
  public void setId(Long id) {
      this.id = id;
  }

  public Doctor getDoctor() {
      return doctor;
  }
  public void setDoctor(Doctor doctor) {
      this.doctor = doctor;
  }

  public Patient getPatient() {
      return patient;
  }
  public void setPatient(Patient patient) {
      this.patient = patient;
  }

  public LocalDateTime getAppointmentTime() {
      return appointmentTime;
  }
  public void setAppointmentTime(LocalDateTime appointmentTime) {
      this.appointmentTime = appointmentTime;
  }

  public int getStatus() {
      return status;
  }
  public void setStatus(int status) {
      this.status = status;
  }
  // 10. Getters and Setters:
  //    - Standard getter and setter methods are provided for accessing and modifying the fields: id, doctor, patient, appointmentTime, status, etc.

}

