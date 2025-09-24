# MySQL Database Design

## Table: appointments
- id: LONG, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

## Table: patients
- id: LONG, Primary Key, Auto Increment
- name: STRING, Not Null
- email: STRING, Not Null
- password: STRING, Not Null
- phone: STRING. Not Null
- address: STRING, Not Null

## Table: doctors
- id: LONG, Primary Key, Auto Increment
- name: STRING, Not Null
- email: STRING, Not Null
- specialty: STRING, Not Null
- password: STRING, Not Null
- phone: STRING, Not Null
- abailableTimes: LIST<STRING>

## Table: admin
- id: LONG, Primary Key, Auto Increment
- username: STRING, Not Null
- password_hash: STRING, Not Null

# MongoDB Collection Design

## Collection: prescriptions

```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
}
```
