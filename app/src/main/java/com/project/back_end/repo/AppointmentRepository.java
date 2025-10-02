package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.transaction.Transactional;

// 1. Extend JpaRepository:
//    - The repository extends JpaRepository<Appointment, Long>, which gives it basic CRUD functionality.
//    - The methods such as save, delete, update, and find are inherited without the need for explicit implementation.
//    - JpaRepository also includes pagination and sorting features.
@Repository
// @Repository annotation:
//  - The @Repository annotation marks this interface as a Spring Data JPA repository.
//  - Spring Data JPA automatically implements this repository, providing the necessary CRUD functionality and custom queries defined in the interface.
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
   // 2. Custom Query Methods:
   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH d.availableTimes WHERE d.id = :doctorId AND a.appointmentTime BETWEEN :start AND :end")
   public List<Appointment> findByDoctorIdAndAppointmentTimeBetween(Long doctorId, LocalDateTime start, LocalDateTime end);
   //    - **findByDoctorIdAndAppointmentTimeBetween**:
   //      - This method retrieves a list of appointments for a specific doctor within a given time range.
   //      - The doctor’s available times are eagerly fetched to avoid lazy loading.
   //      - Return type: List<Appointment>
   //      - Parameters: Long doctorId, LocalDateTime start, LocalDateTime end
   //      - It uses a LEFT JOIN to fetch the doctor’s available times along with the appointments.
   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE d.id = :doctorId AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%')) AND a.appointmentTime BETWEEN :start AND :end")
   public List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(Long doctorId, String patientName, LocalDateTime start, LocalDateTime end);
   //    - **findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween**:
   //      - This method retrieves appointments for a specific doctor and patient name (ignoring case) within a given time range.
   //      - It performs a LEFT JOIN to fetch both the doctor and patient details along with the appointment times.
   //      - Return type: List<Appointment>
   //      - Parameters: Long doctorId, String patientName, LocalDateTime start, LocalDateTime end
   @Modifying
   @Transactional
   @Query("DELETE FROM Appointment a WHERE a.doctor.id = :doctorId")
   public void deleteAllByDoctorId(Long doctorId);
   //    - **deleteAllByDoctorId**:
   //      - This method deletes all appointments associated with a particular doctor.
   //      - It is marked as @Modifying and @Transactional, which makes it a modification query, ensuring that the operation is executed within a transaction.
   //      - Return type: void
   //      - Parameters: Long doctorId
   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE p.id = :patientId")
   public List<Appointment> findByPatientId(Long patientId);
   //    - **findByPatientId**:
   //      - This method retrieves all appointments for a specific patient.
   //      - Return type: List<Appointment>
   //      - Parameters: Long patientId
   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE p.id = :patientId AND a.status = :status ORDER BY a.appointmentTime ASC")
   public List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(Long patientId, int status);
   //    - **findByPatient_IdAndStatusOrderByAppointmentTimeAsc**:
   //      - This method retrieves all appointments for a specific patient with a given status, ordered by the appointment time.
   //      - Return type: List<Appointment>
   //      - Parameters: Long patientId, int status
   @Query("SELEECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND p.id = :patientId")
   public List<Appointment> filterByDoctorNameAndPatientId(String doctorName, Long patientId);
   //    - **filterByDoctorNameAndPatientId**:
   //      - This method retrieves appointments based on a doctor’s name (using a LIKE query) and the patient’s ID.
   //      - Return type: List<Appointment>
   //      - Parameters: String doctorName, Long patientId
   @Query("SELECT a FROM Appointment a LEFT JOIN FETCH a.doctor d LEFT JOIN FETCH a.patient p WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) AND p.id = :patientId AND a.status = :status")
   public List<Appointment> filterByDoctorNameAndPatientIdAndStatus(String doctorName, Long patientId, int status);
   //    - **filterByDoctorNameAndPatientIdAndStatus**:
   //      - This method retrieves appointments based on a doctor’s name (using a LIKE query), patient’s ID, and a specific appointment status.
   //      - Return type: List<Appointment>
   //      - Parameters: String doctorName, Long patientId, int status
   @Modifying
   @Transactional
   @Query("UPDATE Appointment a SET a.status = :status WHERE a.id = :id")
   public void updateStatus(int status, long id);
   //    - **updateStatus**:
   //      - This method updates the status of a specific appointment based on its ID.
   //      - Return type: void
   //      - Parameters: int status, long id
}
