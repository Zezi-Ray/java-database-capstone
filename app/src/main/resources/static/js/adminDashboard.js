  // adminDashboard.js

  /* This script handles the admin dashboard functionality for managing doctors:
  - Loads all doctor cards
  - Filters doctors by name, time, or specialty
  - Adds a new doctor via modal form */

  import { getDoctors, filterDoctors, saveDoctor } from '../services/doctorService.js';
  import { openModal } from './modals.js';
  import { createDoctorCard } from './doctorCard.js';

window.onload = async function() {
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
      const contentDiv = document.getElementById("content");
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

  document.getElementById("searchBar").addEventListener("input", filterDoctorsOnChange);
  document.getElementById("timeFilter").addEventListener("change", filterDoctorsOnChange);
  document.getElementById("specialtyFilter").addEventListener("change", filterDoctorsOnChange);
  // Attach 'input' and 'change' event listeners to the search bar and filter dropdowns
  // On any input change, call filterDoctorsOnChange()

  // Function: filterDoctorsOnChange
  // Purpose: Filter doctors based on name, available time, and specialty
  async function filterDoctorsOnChange() {
    const search = document.getElementById("searchBar").value.trim();
    const time = document.getElementById("timeFilter").value;
    const specialty = document.getElementById("specialtyFilter").value;
    // Read values from the search bar and filters
    const name = search.length > 0 ? search : null;
    const timeValue = time.length > 0 ? time : null;
    const specialtyValue = specialty.length > 0 ? specialty : null;
    // Normalize empty values to null
    
    try {
      const doctor = await filterDoctors(name, timeValue, specialtyValue);
      // Call filterDoctors(name, time, specialty) from the service
      if (doctor.length > 0) {
        renderDoctorCards(doctor);
        // If doctors are found:
        // - Render them using createDoctorCard()
      } else {
        document.getElementById("content").innerHTML = "No doctors found with the given filters."
      }
      // If no doctors match the filter:
      // - Show a message: "No doctors found with the given filters."
    } catch (error) {
      alert("ERROR");
    }
    // Catch and display any errors with an alert
  }

  // Function: renderDoctorCards
  // Purpose: Render a list of doctor cards to the content area
  function renderDoctorCards(doctor) {
    const contentDiv = document.getElementById("content");
    contentDiv.innerHTML = "";
    // Clear the content area
    doctor.forEach(doctor => {
    // Loop through the doctors and apend each card to the content area
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
      if (response.ok) {
        alert("Doctor added successfully!");
        document.getElementById("modal").style.display = "none";
        window.location.reload();
        // If save is successful:
        // - Show a success message
        // - Close the modal and reload the page
      } else {
        alert("Failed to add doctor. Please try again.");
      }
    } catch (error) {
      alert("An error occurred while adding the doctor.");
    }
    // If saving fails, show an error message
  }
}