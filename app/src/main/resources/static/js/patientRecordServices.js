// patientRecordServices.js
import { getPatientAppointments } from "./services/patientServices.js";
import { createPatientRecordRow } from './components/patientRecordRow.js';
import { getAllAppointments } from "./services/appointmentRecordService.js";


const tableBody = document.getElementById("patientTableBody");
const token = localStorage.getItem("token");

const urlParams = new URLSearchParams(window.location.search);
const patientId = urlParams.get("id");
const doctorId = urlParams.get("doctorId");

document.addEventListener("DOMContentLoaded", initializePage);

async function initializePage() {
  try {
    if (!token) throw new Error("No token found");
    const role = localStorage.getItem("userRole");
    let appointments = [];

    if (role === "patient") {
      appointments = await getPatientAppointments(patientId, token) || [];
    } else if (role === "doctor") {
      const name = urlParams.get("name") || "all";
      const all = await getAllAppointments(name, "all", token) || [];
      appointments = all
        .filter(app => (app.patientId ?? app.patient?.id) == patientId)
        .map(app => ({
          ...app,
          patientId: app.patientId ?? app.patient?.id,
          appointmentDate:
            app.appointmentDate ??
            (app.appointmentTime ? app.appointmentTime.split("T")[0] : null),
        }));
    } else {
      console.warn("Unsupported role:", role);
    }

    renderAppointments(appointments);

  } catch (error) {
    console.error("Error loading appointments:", error);
    alert("❌ Failed to load your appointments.");
  }
}

function renderAppointments(appointments) {
  tableBody.innerHTML = "";

  const actionTh = document.querySelector("#patientTable thead tr th:last-child");
  if (actionTh) {
    actionTh.style.display = "table-cell"; // Always show "Actions" column
  }

  if (!appointments.length) {
    tableBody.innerHTML = `<tr><td colspan="5" style="text-align:center;">No Appointments Found</td></tr>`;
    return;
  }

  appointments.forEach(appointment => {
    const row = createPatientRecordRow(appointment);
    tableBody.appendChild(row);
  });
}
