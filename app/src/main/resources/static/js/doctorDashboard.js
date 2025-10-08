// doctorDashboard.js

import {getAllAppointments} from './services/appointmentRecordService.js';
// Import getAllAppointments to fetch appointments from the backend
import {createPatientRow} from './components/patientRows.js';
// Import createPatientRow to generate a table row for each patient appointment

const tableBody = document.getElementById('patientTableBody');
// Get the table body where patient rows will be added
let selectedDate = '';
// Initialize selectedDate with today's date in 'YYYY-MM-DD' format
const token = localStorage.getItem('token');
// Get the saved token from localStorage (used for authenticated API calls)
let patientName = null;
// Initialize patientName to null (used for filtering by name)

// Add an 'input' event listener to the search bar
document.getElementById('searchBar').addEventListener('input', (event) => {
  const value = event.target.value.trim();
  // Trim and check the input value
  patientName = value.length > 0 ? value : null;
  // If not empty, use it as the patientName for filtering
  // Else, reset patientName to null (as expected by backend)
  loadAppointments();
  // Reload the appointments list with the updated filter
})

// Add a click listener to the "Today" button
document.getElementById('todayButton').addEventListener('click', () => {
  selectedDate = new Date().toISOString().split('T')[0];
  // Set selectedDate to today's date
  document.getElementById('datePicker').value = selectedDate;
  // Update the date picker UI to match
  loadAppointments();
  // Reload the appointments for today
});

// Add a change listener to the date picker input
document.getElementById('datePicker').addEventListener('change', (event) => {
  selectedDate = event.target.value.trim();
  // Update selectedDate with the chosen date
  loadAppointments();
  // Reload the appointments for that specific date
});

// Function: loadAppointments
// Purpose: Fetch and display appointments based on selected date and optional patient name
async function loadAppointments() {
  try {
    const appointments = await getAllAppointments(patientName, selectedDate, token);
    // Step 1: Call getAllAppointments with selectedDate, patientName, and token
    tableBody.innerHTML = '';
    // Step 2: Clear the table body content before rendering new rows
    if (appointments.length === 0) {
      // Step 3: If no appointments are returned:
      const noDataRow = document.createElement('tr');
      noDataRow.innerHTML = `
        <td colspan="5" style="text-align: center;">No Appointments found for today.</td>
      `;
      tableBody.appendChild(noDataRow);
      return;
      // - Display a message row: "No Appointments found for today."
    } else {
      // Step 4: If appointments exist:
      appointments.forEach(appointment => {
        // - Loop through each appointment and construct a 'patient' object with id, name, phone, and email
        const row = createPatientRow({
          id: appointment.patient.id,
          name: appointment.patient.name,
          phone: appointment.patient.phone,
          email: appointment.patient.email
        }, appointment.id, appointment.doctor.id);
        // - Call createPatientRow to generate a table row for the appointment
        tableBody.appendChild(row);
        // - Append each row to the table body
      });
    }
  } catch (error) {
    const errorRow = document.createElement('tr');
    errorRow.innerHTML = `
      <td colspan="5" style="text-align: center; color: red;">Error loading appointments. Try again later.</td>
    `;
    tableBody.appendChild(errorRow);
  }
}

// When the page fully loaded 
document.addEventListener('DOMContentLoaded', () => {
  renderContent();
  // Call renderContent() to set up the initial UI layout
  selectedDate = '';
  document.getElementById('datePicker').value = selectedDate;
  loadAppointments();
  // Call loadAppointments() to display today's appointments by default
});