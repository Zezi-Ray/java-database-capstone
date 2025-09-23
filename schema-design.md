# MySQL Database Design

## Table: appointments
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)
- reason: TEXT

## Table: patients
- id: INT, Primary Key, Auto Increment
- name: CHAR, Not Null
- birthday: DATE, Not Null
- email: CHAR, Not Null, Unique
- password_hash: CHAR, Not Null

## Table: doctors
- id: INT, Primary Key, Auto Increment
- name: CHAR, Not Null
- email: CHAR, Not Null, Unique
- specialty: CHAR, Not Null
- password_hash: CHAR, Not Null
- room: INT, Not Null

## Table: admin
- id: INT, Primary Key, Auto Increment
- username: CHAR, Not Null, Unique
- password_hash: CHAR, Not Null

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
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```

## Collection: feedbacks

```json
{
  "patientId": 101,
  "doctorId": 12,
  "appointmentId": 5001,
  "rating": 4,
  "comments": "Doctor was attentive and explained options clearly.",
  "tags": ["courtesy", "clear-explanations"],
  "anonymous": false,
  "createdAt": "2025-09-23T19:20:00Z",
}
```

## Collection: logs

```json
{
  { "actorType": "admin",   "actorId": 1,   "action": "LOGIN_SUCCESS" },
  { "actorType": "admin",   "actorId": 1,   "action": "DOCTOR_CREATED",       "entityType": "doctor",      "entityId": 12 },
  { "actorType": "patient", "actorId": 101, "action": "SIGNUP_SUCCESS" },
  { "actorType": "patient", "actorId": 101, "action": "APPOINTMENT_CREATED",  "entityType": "appointment", "entityId": 5001, "meta": { "doctorId": 12, "scheduledAt": "2025-09-25T10:00:00Z"} },
  { "actorType": "admin",   "actorId": 1,   "action": "APPOINTMENT_CANCELLED","entityType": "appointment", "entityId": 5001 }
}
```
