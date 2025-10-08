  // adminDashboard.js

  /* This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form */

import { getDoctors, filterDoctors, saveDoctor } from './services/doctorServices.js';
import { openModal } from './components/modals.js';
import { createDoctorCard } from './components/doctorCard.js';

const searchBar = document.getElementById("searchBar");
const timeFilter = document.getElementById("timeFilter");
const specialtyFilter = document.getElementById("specialtyFilter");
const contentDiv = document.getElementById("content");

  document.getElementById("addDocBtn").addEventListener("click", () => {
  // Attach a click listener to the "Add Doctor" button
    openModal('addDoctor');
    // When clicked, it opens a modal form using openModal('addDoctor')
  })

  // Function: loadDoctorCards
  // Purpose: Fetch all doctors and display them as cards
  async function loadDoctorCards() {
  // On page load, call loadDoctorCards to fetch and display all doctors
    try {
      const doctor = await getDoctors();
      // Call getDoctors() from the service layer
      contentDiv.innerHTML = "";
      // Clear the current content area
      doctor.forEach (doctor => {
      // For each doctor returned
        const card = createDoctorCard(doctor);
        // Create a doctor card using createDoctorCard()
        contentDiv.appendChild(card);
        // Append it to the content div
      }); 
    } catch (error) {
      console.error("Failed to load doctors:", error);
    }
    // Handle any fetch errors by logging them
  }
  loadDoctorCards();

  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("timeFilter").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("specialtyFilter").addEventListener("change", filterDoctorsOnChange);
  // Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  // On any input change, call filterDoctorsOnChange()

  // Function: filterDoctorsOnChange
  // Purpose: Filter doctors based on name, available time, and specialty
  async function filterDoctorsOnChange() {
    const name = searchBar?.value.trim() || null;
    const time = timeFilter?.options[timeFilter.selectedIndex]?.value || null;
    const specialty = specialtyFilter?.options[specialtyFilter.selectedIndex]?.value || null;

    try {
      const doctors = await filterDoctors(name, time, specialty);
      console.debug('filterDoctors result', { name, time, specialty, doctors });
      renderDoctorCards(doctors);
    } catch (error) {
      console.error("Error filtering doctors:", error);
      alert("ERROR");
    }
  }

  // Function: renderDoctorCards
  // Purpose: Render a list of doctor cards to the content area
  function renderDoctorCards(doctors) {
    contentDiv.innerHTML = "";
    if (!Array.isArray(doctors) || doctors.length === 0) {
      contentDiv.innerHTML = '<p class="no-results">No doctors found with the current filters.</p>';
      return;
    }
    doctors.forEach(doctor => {
      const card = createDoctorCard(doctor);
      contentDiv.appendChild(card);
    });
  }

  // Function: adminAddDoctor
  // Purpose: Collect form data and add a new doctor to the system
  async function adminAddDoctor() {
    const name = document.getElementById("doctorName").value.trim();
    const specialty = document.getElementById("specialization").value.trim();
    const email = document.getElementById("doctorEmail").value.trim();
    const password = document.getElementById("doctorPassword").value.trim();
    const phone = document.getElementById("doctorPhone").value.trim();
    const availability = document.querySelectorAll('input[name="availability"]:checked');
    const availableTimes = Array.from(availability).map(cb => cb.value);
    // Collect input values from the modal form
    // - Includes name, email, phone, password, specialty, and available times
    const token = localStorage.getItem("token");
    if (!token) {
      alert("No auth token found. Please log in again.");
      return;
    }
    // Retrieve the authentication token from localStorage
    // - If no token is found, show an alert and stop execution
    const doctor = { name, email, phone, password, specialty, availableTimes };
    // Build a doctor object with the form values
    try {
      const response = await saveDoctor(doctor, token);
      // Call saveDoctor(doctor, token) from the service
      if (response.success) {
        alert("Doctor added successfully!");
        document.getElementById("modal").style.display = "none";
        window.location.reload();
        // If save is successful:
        // - Show a success message
        // - Close the modal and reload the page
      } else {
        alert(response.message || "Failed to add doctor. Please try again.");
      }
    } catch (error) {
      alert("An error occurred while adding the doctor.");
    }
    // If saving fails, show an error message
  }

  window.adminAddDoctor = adminAddDoctor;
