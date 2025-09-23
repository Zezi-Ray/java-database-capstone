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
