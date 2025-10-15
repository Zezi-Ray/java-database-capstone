/* Step-by-Step Explanation of Header Section Rendering This code dynamically renders the header section of the page based on the user's role, session status, and available actions (such as login, logout, or role-switching). */
// header.js

function renderHeader() {
// 1. Define the `renderHeader` Function * The `renderHeader` function is responsible for rendering the entire header based on the user's session, role, and whether they are logged in.
  const headerDiv = document.getElementById("header");
  // 2. Select the Header Div * The `headerDiv` variable retrieves the HTML element with the ID `header`, where the header content will be inserted.
  if (window.location.pathname.endsWith("/")) {
  // 3. Check if the Current Page is the Root Page * The `window.location.pathname` is checked to see if the current page is the root (`/`). If true, the user's session data (role) is removed from `localStorage`, and the header is rendered without any user-specific elements (just the logo and site title).
    localStorage.removeItem("userRole");
    headerDiv.innerHTML = `
      <header class="header">
        <div class="logo-section">
          <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
          <span class="logo-title">Hospital CMS</span>
        </div>
      </header>`;
    return;
  }
  // 4. Retrieve the User's Role and Token from LocalStorage * The `role` (user role like admin, patient, doctor) and `token` (authentication token) are retrieved from `localStorage` to determine the user's current session.
  const role = localStorage.getItem("userRole");
  const token = localStorage.getItem("token");
  // 5. Initialize Header Content * The `headerContent` variable is initialized with basic header HTML (logo section), to which additional elements will be added based on the user's role.
  let headerContent = `<header class="header">
    <div class="logo-section">
      <img src="../assets/images/logo/logo.png" alt="Hospital CRM Logo" class="logo-img">
      <span class="logo-title">Hospital CMS</span>
    </div>
    <nav>`;
  // 6. Handle Session Expiry or Invalid Login * If a user with a role like `loggedPatient`, `admin`, or `doctor` does not have a valid `token`, the session is considered expired or invalid. The user is logged out, and a message is shown.
  if ((role === "loggedPatient" || role === "admin" || role === "doctor") && !token) {
    localStorage.removeItem("userRole");
    alert("Session expired or invalid login. Please log in again.");
    window.location.href = "/";
    return;
  } 
  // 7. Add Role-Specific Header Content * Depending on the user's role, different actions or buttons are rendered in the header
  else if (role === "admin") {
    // **Admin**: Can add a doctor and log out.
    headerContent += `
      <button id="addDocBtn" class="adminBtn">Add Doctor</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "doctor") {
    // **Doctor**: Has a home button and log out.
    headerContent += `
      <button class="adminBtn"  onclick="selectRole('doctor')">Home</button>
      <a href="#" onclick="logout()">Logout</a>`;
  } else if (role === "patient") {
    // **Patient**: Shows login and signup buttons.
    headerContent += `
      <button id="patientLogin" class="adminBtn">Login</button>
      <button id="patientSignup" class="adminBtn">Sign Up</button>`;
  } else if (role === "loggedPatient") {
    // **LoggedPatient**: Has home, appointments, and logout options.
    headerContent += `
      <button id="home" class="adminBtn" onclick="selectRole('loggedPatient'); window.location.href='/pages/loggedPatientDashboard.html'">Home</button>
      <button id="patientAppointments" class="adminBtn" onclick="selectRole('loggedPatient'); window.location.href='/pages/patientAppointments.html'">Appointments</button>
      <a href="#" onclick="logoutPatient()">Logout</a>`;
  } 
  // 8. Close the Header Section 
  headerContent += `</nav></header>`;
  // 9. Render the Header Content
  headerDiv.innerHTML = headerContent;
  // 10. Attach Event Listeners to Header Buttons * Call `attachHeaderButtonListeners` to add event listeners to any dynamically created buttons in the header (e.g., login, logout, home).
  
}

// 11. AttachHeaderButtonListeners Adds event listeners to login buttons for "Doctor" and "Admin" roles. If clicked, it opens the respective login modal.

// 12. logout Removes user session data and redirects the user to the root page.
function logout() {
  localStorage.removeItem("userRole");
  localStorage.removeItem("token");
  window.location.href = "/";
}
// 13. logoutPatient Removes the patient's session token and redirects to the patient dashboard.
function logoutPatient() {
  localStorage.removeItem("token");
  localStorage.setItem("userRole", "patient");
  window.location.href = "/pages/patientDashboard.html";
}
// 14. **Render the Header**: Finally, the `renderHeader()` function is called to initialize the header rendering process when the page loads.
renderHeader();

document.addEventListener('click', (e) => {
  if(e.target.matches('.logo-img')) {
    localStorage.removeItem("userRole");
    localStorage.removeItem("token");
    window.location.href = '/';
  }
});
