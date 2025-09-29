// index.js

import { openModal } from '../utils/util.js';
import { API_BASE_URL } from '../config/config.js';
// Import the base API URL from the config file

const ADMIN_API = API_BASE_URL + '/admin';
const DOCTOR_API = API_BASE_URL + '/doctor/login';
// Define constants for the admin and doctor login API endpoints using the base URL

window.onload = function() {
// Use the window.onload event to ensure DOM elements are available after page load
  const adminLoginBn = document.getElementById('adminLoginBtn');
  const doctorLoginBn = document.getElementById('doctorLoginBtn');
  // Select the "adminLogin" and "doctorLogin" buttons using getElementById

  if (adminLoginBn) {
  // If the admin login button exists
    adminLoginBn.addEventListener('click', () => {
    // Add a click event listener that calls openModal('adminLogin') to show the admin login modal
      openModal('adminLogin');
    });
  }
  
  if (doctorLoginBn) {
  // If the doctor login button exists
    doctorLoginBn.addEventListener('click', () => {
    // Add a click event listener that calls openModal('doctorLogin') to show the doctor login modal
      openModal('doctorLogin');
    });
  }

  async function adminLoginHandler() {
  // Define a function named adminLoginHandler on the global window object. This function will be triggered when the admin submits their login credentials
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    // Step 1: Get the entered username and password from the input fields
    const admin = {username, password};
    // Step 2: Create an admin object with these credentials
    try {
      const response = fetch(ADMIN_API, {
      // Step 3: Use fetch() to send a POST request to the ADMIN_API endpoint
        method: 'POST',
        // Set method to POST
        headers: {
          'Content-Type': 'application/json'
          // Add headers with 'Content-Type: application/json
        },
        body: JSON.stringify(admin)
        // Convert the admin object to JSON and send in the body
      })
      if (response.ok) {
      // Step 4: If the response is successful
        const data = await response.json();
        const token = data.token;
        // Parse the JSON response to get the token
        localStorage.setItem('token', token);
        // Store the token in localStorage
        selectRole('admin');
        // Call selectRole('admin') to proceed with admin-specific behavior
      } else {
        alert('Login failed. Please check your credentials.');
      }
    } catch (error) {
      alert('An error occurred. Please try again later.');
      // Step 6: Wrap everything in a try-catch to handle network or server errors. Show a generic error message if something goes wrong
    }
  }
  
  async function doctorLoginHandler() {
  // Define a function named doctorLoginHandler on the global window object. This function will be triggered when a doctor submits their login credentials
    const email = document.getElementById("email").value;
    const password = document.getElementById("password").value;
    // Step 1: Get the entered email and password from the input fields
    const doctor = {email, password};
    // Step 2: Create a doctor object with these credentials
    try {
      const respones = fetch(DOCTOR_API, {
      // Step 3: Use fetch() to send a POST request to the DOCTOR_API endpoint
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(doctor)
        // Include headers and request body similar to admin login
      })
      if (respones.ok) {
      // Step 4: If login is successful
        const data = await respones.json();
        const token = data.token;
        // Parse the JSON response to get the token
        localStprage.setItem('token', token);
        // Store the token in localStorage
        selectRole('doctor');
        // Call selectRole('doctor') to proceed with doctor-specific behavior
      } else {
        alert('Login failed. Please check your credentials.');
      }
    } catch (error) {
      alert('An error occurred. Please try again later.');
      // Step 6: Wrap in a try-catch block to handle errors gracefully. Log the error to the console and show a generic error message
    }
  }
}