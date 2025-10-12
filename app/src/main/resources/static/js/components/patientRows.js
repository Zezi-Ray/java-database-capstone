// patientRows.js
export function createPatientRow(patient, appointmentId, doctorId) {
 
  const tr = document.createElement("tr");

  console.log("CreatePatientRow :: ", doctorId)
  tr.innerHTML = `
      <td class="patient-id">${patient.id}</td>
      <td>${patient.name}</td>
      <td>${patient.phone}</td>
      <td>${patient.email}</td>
    `;

  // Attach event listeners
  tr.querySelector(".patient-id").addEventListener("click", () => {
    window.location.href = `/pages/patientRecord.html?id=${patient.id}&doctorId=${doctorId}`;
  });

  return tr;
}
