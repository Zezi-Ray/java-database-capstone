// patientRecordRow.js
export function createPatientRecordRow(appointment) {
  if (!appointment) return document.createElement("tr");

  const status = appointment.status;
  const isEditable = status === 1;
  const mode = isEditable ? "edit" : "view";

  const patientId = appointment.patientId ?? appointment.patient?.id ?? "";
  const patientName = appointment.patientName ?? appointment.patient?.name ?? "";
  const doctorId = appointment.doctorId ?? appointment.doctor?.id ?? "";
  const doctorName = appointment.doctorName ?? appointment.doctor?.name ?? "";

  const appointmentDate = appointment.appointmentDate
    ?? (appointment.appointmentTime ? appointment.appointmentTime.split("T")[0] : "");

  const startTime = appointment.appointmentTimeOnly
    ? appointment.appointmentTimeOnly.toString().slice(0,5)
    : (appointment.appointmentTime ? appointment.appointmentTime.split("T")[1]?.slice(0,5) : "");
  const endTime = appointment.endTime
    ? appointment.endTime.split("T")[1]?.slice(0,5)
    : "";
  const appointmentRange = startTime && endTime ? `${startTime}-${endTime}` : startTime;

  const tr = document.createElement("tr");
  tr.innerHTML = `
      <td>${appointmentDate || "-"}</td>
      <td>${appointment.id ?? "-"}</td>
      <td>${patientId || "-"}</td>
      <td>
        <img src="../assets/images/addPrescriptionIcon/addPrescription.png" alt="Prescription" class="prescription-btn">
      </td>
      <td>
        <img src="../assets/images/edit/edit.png" alt="Edit" class="edit-btn">
      </td>
    `;

  const prescriptionBtn = tr.querySelector(".prescription-btn");
  prescriptionBtn?.addEventListener("click", () => {
    const query = new URLSearchParams({
      appointmentId: appointment.id,
      patientId,
      patientName,
      doctorName,
      doctorId,
      appointmentDate : appointmentDate ?? "",
      appointmentTime: appointmentRange ?? "",
      appointmentStatus: status ?? "",
      role: localStorage.getItem("userRole") ?? "",
    }).toString();
    window.location.href = `/pages/addPrescription.html?${query}`;
  });

  const editBtn = tr.querySelector(".edit-btn");
  editBtn?.addEventListener("click", () => {
    const params = new URLSearchParams({
      appointmentId: appointment.id,
      patientId,
      patientName,
      doctorName,
      doctorId,
      appointmentDate: appointmentDate ?? "",
      appointmentTime: appointmentRange ?? "",
      appointmentStatus: status ?? "",
      role: localStorage.getItem("userRole") ?? "",
    });
    window.location.href = `/pages/updateAppointment.html?${params.toString()}`;
  });

  return tr;
}
