//doctorServices.js

import { API_BASE_URL } from "../config/config";
// Import the base API URL from the config file
const DOCTOR_API = API_BASE_URL + '/doctor';
// Define a constant DOCTOR_API to hold the full endpoint for doctor-related actions

  // Function: getDoctors
  // Purpose: Fetch the list of all doctors from the API
  export async function getDoctors() {
    try {
      const response = await fetch(DOCTOR_API, {
      // Use fetch() to send a GET request to the DOCTOR_API endpoint
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        },
        body: null
      })
      const data = await response.json();
      // Convert the response to JSON
      return data.doctors;
      // Return the 'doctors' array from the response
    } catch (error) {
      console.error('Error fetching doctors:', error);
      return [];
      // If there's an error (e.g., network issue), log it and return an empty array
    }
  }

  // Function: deleteDoctor
  // Purpose: Delete a specific doctor using their ID and an authentication token
  export async function deleteDoctor(id, token) {
    try {
      const response = await fetch(DOCTOR_API + `/${id}/${token}`, {
      // Use fetch() with the DELETE method
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json'
        },
        body: null
      })
      const data = await response.json();
      // Convert the response to JSON
      if (response.ok) {
        return { success: true, message: data.message };
      } else {
        return { success: false, message: data.message };
      }
      // Return an object with:
      //  - success: true if deletion was successful
      //  - message: message from the server
    } catch (error) {
      console.error('Error deleting doctor:', error);
      return { success: false, message: 'An error occurred' };
    }
    // If an error occurs, log it and return a default failure response
  }

  // Function: saveDoctor 
  // Purpose: Save (create) a new doctor using a POST request
  export async function saveDoctor(doctor, token) {
    try {
      const response = await fetch(DOCTOR_API + `/${token}`, {
      // Use fetch() with the POST method
      // URL includes the token in the path
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        // Set headers to specify JSON content type
        body: JSON.stringify(doctor)
      })
      // Convert the doctor object to JSON in the request body
      if (response.ok) {
        const data = await response.json();
        return { success: true, message: data.message };
      } else {
        const data = await response.json();
        return { success: false, message: data.message };
      }
      // Parse the JSON response and return:
      //  - success: whether the request succeeded
      //  - message: from the server
    } catch (error) {
      console.error('Error saving doctor:', error);
      return { success: false, message: 'An error occurred' };
    }
    // Catch and log errors. Return a failure response if an error occurs
  }

  // Function: filterDoctors
  // Purpose: Fetch doctors based on filtering criteria (name, time, and specialty)
  export async function filterDoctors(name, time, specialty) {
    try {
      const response = await fetch(DOCTOR_API + `/filter/${name}/${time}/${specialty}`, {
      // Use fetch() with the GET method
      // Include the name, time, and specialty as URL path parameters
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        },
        body: null
      })
      if (response.ok) {
        const data = await response.json();
        return Array.isArray(data.doctors) ? data.doctors : [];
      } else {
        console.error('Error filtering doctors:', response.statusText);
        return { doctors: [] };
      }
      // Check if the response is OK
      //  - If yes, parse and return the doctor data
      //  - If no, log the error and return an object with an empty 'doctors' array
    } catch (error) {
      alert('An error occurred. Please try again later.');
      console.error('Error filtering doctors:', error);
      return { doctors: [] };
    }
    // Catch any other errors, alert the user, and return a default empty result
  }