// updateAppointment.js
import { updateAppointment } from "../js/services/appointmentRecordService.js";
import { getDoctors } from "../js/services/doctorServices.js";
import { API_BASE_URL } from './config/config.js'

document.addEventListener("DOMContentLoaded", initializePage);

async function initializePage() {
  const token = localStorage.getItem("token"); // Assuming token is stored in localStorage
  // Get appointmentId and patientId from the URL query parameters
  const urlParams = new URLSearchParams(window.location.search);
  const appointmentId = urlParams.get("appointmentId");
  const patientId = urlParams.get("patientId");
  const doctorId = urlParams.get("doctorId");
  const patientName = urlParams.get("patientName");
  const doctorName = urlParams.get("doctorName");
  const appointmentDate = urlParams.get("appointmentDate");
  const appointmentTime = urlParams.get("appointmentTime");
  const status = urlParams.get("appointmentStatus");
  const role = urlParams.get("role");

  console.log(doctorId)
  if (!token || !patientId) {
    alert("Missing session data, redirecting to appointments page.");
    window.location.href = "/pages/patientAppointments.html";
    return;
  }

  // get doctor to display only the available time of doctor
  getDoctors()
    .then(doctors => {
      // Find the doctor by the ID from the URL
      const doctor = doctors.find(d => d.id == doctorId);
      if (!doctor) {
        alert("Doctor not found.");
        return;
      }

      // Fill the form with the appointment data passed in the URL
      document.getElementById("patientName").value = patientName || "You";
      document.getElementById("doctorName").value = doctorName;
      document.getElementById("appointmentDate").value = appointmentDate;
      document.getElementById("appointmentTime").value = appointmentTime;
      document.getElementById("status").value = status;

      const dateInput = document.getElementById("appointmentDate");
      const timeInput = document.getElementById("appointmentTime");
      const statusSelect = document.getElementById("status");
      const deleteBtn = document.getElementById("deleteAppointment");
      if (role === "loggedPatient") {
        statusSelect.disabled = true; // Patients cannot change status
      } else if (role === "doctor") {
        dateInput.disabled = true; // Doctors cannot change date
        timeInput.disabled = true; // Doctors cannot change time
        statusSelect.disabled = false; // Doctors can change status
        deleteBtn.style.display = "none"; // Hide delete button for doctors
        
      }

      const timeSelect = document.getElementById("appointmentTime");
      doctor.availableTimes.forEach(time => {
        const option = document.createElement("option");
        option.value = time;
        option.textContent = time;
        timeSelect.appendChild(option);
      });

      // Handle form submission for updating the appointment
      document.getElementById("updateAppointmentForm").addEventListener("submit", async (e) => {
        e.preventDefault(); // Prevent default form submission

        const date = document.getElementById("appointmentDate").value;
        const time = document.getElementById("appointmentTime").value;
        const startTime = time.split('-')[0];
        if (!date || !time) {
          alert("Please select both date and time.");
          return;
        }

        const updatedAppointment = {
          id: appointmentId,
          doctor: { id: doctor.id },
          patient: { id: patientId },
          appointmentTime: `${date}T${startTime}:00`,
          status: Number(statusSelect.value ?? status)
        };

        const updateResponse = await updateAppointment(updatedAppointment, token);

        if (updateResponse.success) {
          alert("Appointment updated successfully!");
          if (role === "loggedPatient") {
            window.location.href = "/pages/patientAppointments.html"; // Redirect back to the appointments page
          } else if (role === "doctor") {
            window.location.href =`/pages/patientRecord.html?id=${patientId}&doctorId=${doctorId}`;
            // Redirect back to the doctor's appointments page
          }
        } else {
          alert("❌ Failed to update appointment: " + updateResponse.message);
        }
      });
    })
    .catch(error => {
      console.error("Error fetching doctors:", error);
      alert("❌ Failed to load doctor data.");
    });

    document.getElementById("backBtn").addEventListener("click", () => {
      window.history.back();
    });

    document.getElementById("deleteAppointment").addEventListener("click", async () => {
      if (!confirm("Are you sure you want to delete this appointment?")) {
        return;
      }
  
      const token = localStorage.getItem("token");
      if (!token) {
        alert("Missing session data, redirecting to appointments page.");
        window.location.href = "/pages/patientAppointments.html";
        return;
      }
  
      try {
        const response = await fetch(`${API_BASE_URL}/appointments/cancel/${appointmentId}/${token}`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        });
  
        if (response.ok) {
          alert("Appointment deleted successfully!");
          window.location.href = "/pages/patientAppointments.html"; // Redirect back to the appointments page
        } else {
          const errorData = await response.json();
          alert("❌ Failed to delete appointment: " + errorData.message);
        }
      } catch (error) {
        console.error("Error deleting appointment:", error);
        alert("❌ An error occurred while deleting the appointment.");
      }
    });
}
