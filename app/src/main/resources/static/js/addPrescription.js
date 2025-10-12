import { getAppointments } from "./components/appointmentRow.js";
import { savePrescription, getPrescription } from "./services/prescriptionServices.js";

document.addEventListener('DOMContentLoaded', async () => {
  const savePrescriptionBtn = document.getElementById("savePrescription");
  const cancelPrescriptionBtn = document.getElementById("cancelPrescriptionBtn");
  const backBtn = document.getElementById("backBtn");
  const patientNameInput = document.getElementById("patientName");
  const medicinesInput = document.getElementById("medicines");
  const dosageInput = document.getElementById("dosage");
  const notesInput = document.getElementById("notes");
  const heading = document.getElementById("heading")
  const urlParams = new URLSearchParams(window.location.search);
  const appointmentId = urlParams.get("appointmentId");
  const mode = urlParams.get("mode");
  const token = localStorage.getItem("token");
  const patientName = urlParams.get("patientName")

  if (heading) {
    if (mode === "view") {
      heading.innerHTML = `View <span>Prescription</span>`;
    } else if (mode === "edit") {
      heading.innerHTML = `Add <span>Prescription</span>`;
    }
  }


  // Pre-fill patient name
  if (patientNameInput && patientName) {
    patientNameInput.value = patientName;
  }

  // Fetch and pre-fill existing prescription if it exists
  if (appointmentId && token) {
    try {
      const response = await getPrescription(appointmentId, token);
      const prescriptions = response?.body?.prescriptions || [];
      if (prescriptions.length > 0) {
        const { patientName, medication, dosage, doctorNotes } = prescriptions[0];
        patientNameInput.value = patientName || "";
        medicinesInput.value = medication || "";
        dosageInput.value = dosage || "";
        notesInput.value = doctorNotes || "";
      }
    } catch (error) {
      console.warn("No existing prescription found or failed to load:", error);
    }
  }
  if (mode === 'view') {
    // Make fields read-only
    patientNameInput.disabled = true;
    medicinesInput.disabled = true;
    dosageInput.disabled = true;
    notesInput.disabled = true;
    savePrescriptionBtn.style.display = "none";  // Hide the save button
    cancelPrescriptionBtn.style.display = "inline-block"; // Show the cancel button
    backBtn.style.display = "none"; // Hide the back button
  }
  backBtn.addEventListener('click', () => {
    window.history.back();
  });

  if (mode === 'edit') {
    if (getAppointments === null) {
      savePrescriptionBtn.style.display = "inline-block";  // Show the save button
    } else {
      savePrescriptionBtn.style.display = "none";  // Hide the save button if appointments exist
    }
    cancelPrescriptionBtn.style.display = "none"; // Hide the cancel button
    backBtn.style.display = "inline-block"; // Show the back button
  }
  cancelPrescriptionBtn.addEventListener('click', () => {
    window.history.back();
  });
  // Save prescription on button click
  savePrescriptionBtn.addEventListener('click', async (e) => {
    e.preventDefault();

    const prescription = {
      patientName: patientNameInput.value,
      medication: medicinesInput.value,
      dosage: dosageInput.value,
      doctorNotes: notesInput.value,
      appointmentId
    };

    const { success, message } = await savePrescription(prescription, token);

    if (success) {
      alert("✅ Prescription saved successfully.");
      selectRole('doctor');
    } else {
      alert("❌ Failed to save prescription. " + message);
    }
  });
});
