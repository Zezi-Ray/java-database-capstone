// Import the overlay function for booking appointments from loggedPatient.js
// Import the deleteDoctor API function to remove doctors (admin role) from doctorServices.js
// Import function to fetch patient details (used during booking) from patientServices.js
import { deleteDoctor } from "../services/doctorServices.js";

export function createDoctorCard(doctor) {
// Function to create and return a DOM element for a single doctor card
  const card = document.createElement("div");
  // Create the main container for the doctor card
  card.classList.add("doctor-card");

  const role = localStorage.getItem("userRole");
  // Retrieve the current user role from localStorage

  const infoDiv = document.createElement("div");
  // Create a div to hold doctor information
  infoDiv.classList.add("doctor-info");

  const name = document.createElement("h3");
  name.textContent = doctor.name;
  // Create and set the doctor's name

  const specialization = document.createElement("p");
  specialization.textContent = `Specialization: ${doctor.specialty}`;
  // Create and set the doctor's specialization

  const email = document.createElement("p");
  email.textContent = `Email: ${doctor.email}`;
  // Create and set the doctor's email

  const availability = document.createElement("p");
  availability.textContent = "Available Times: " + doctor.availableTimes.join(", ");
  // Create and set the doctor's available appointment times

  infoDiv.appendChild(name);
  infoDiv.appendChild(specialization);
  infoDiv.appendChild(email);
  infoDiv.appendChild(availability);
  // Append all info elements to the doctor info container

  const actionsDiv = document.createElement("div");
  actionsDiv.classList.add("card-actions");
  // Create a div to hold action buttons (like delete or book)

  // === ADMIN ROLE ACTIONS ===
  if (role === "admin") {
    const removeBtn = document.createElement("button");
    removeBtn.textContent = "Delete";
    // Create a delete button for admin users

    removeBtn.addEventListener("click", async () => {
    // Add click handler for delete button
      const confirmDelete = confirm(`Are you sure you want to delete Dr. ${doctor.name}?`);
      if (!confirmDelete) return;

      const token = localStorage.getItem("token");
      // Get the admin token from localStorage
      if (!token) {
        alert("Admin token not found. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        const result = await deleteDoctor(doctor.id, token);
        // Call API to delete the doctor
        if (result.success) {
          alert("Doctor deleted successfully.");
          card.remove();
        } else {
          alert("Failed to delete doctor: " + result.message);
        }
        // Show result and remove card if successful
      } catch (error) {
        console.error("Error deleting doctor:", error);
        alert("An error occurred while deleting the doctor.");
      }      
    });
    actionsDiv.appendChild(removeBtn);
    // Add delete button to actions container
  }

  // === PATIENT (NOT LOGGED-IN) ROLE ACTIONS ===
  if (role === "patient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    // Create a book now button

    bookBtn.addEventListener("click", () => {
      alert("Please log in as a patient to book an appointment.");
      // Alert patient to log in before booking
      openModal("patientLogin");
      // Open the login modal
    });
    actionsDiv.appendChild(bookBtn);
    // Add button to actions container
  }

  // === LOGGED-IN PATIENT ROLE ACTIONS ===
  if (role === "loggedPatient") {
    const bookBtn = document.createElement("button");
    bookBtn.textContent = "Book Now";
    // Create a book now button

    bookBtn.addEventListener("click", async () => {
    // Handle booking logic for logged-in patient   
      const token = localStorage.getItem("token");
      if (!token) {
      // Redirect if token not available
        alert("Session expired. Please log in again.");
        window.location.href = "/";
        return;
      }

      try {
        const patient = await fetchPatientDetails(token);
        // Fetch patient details using the token
        if (!patient) {
          alert("Failed to fetch patient details. Please log in again.");
          window.location.href = "/";
          return;
        }

        showBookingOverlay(doctor, patient);
        // Show booking overlay with doctor and patient info
      } catch (error) {
        console.error("Error fetching patient details:", error);
        alert("An error occurred. Please try again.");
      }
    });
    actionsDiv.appendChild(bookBtn);
    // Add button to actions container
  }

  card.appendChild(infoDiv);
  // Append the info section to the main card
  card.appendChild(actionsDiv);
  // Append the actions section to the main card

  return card;
  // Return the complete doctor card element
}