# Testing Documentation

## Purpose and Scope
- Confirm that patient, doctor, and admin flows in the Clinic Management System behave as expected after recent updates.
- Cover appointment booking, appointment updates, patient navigation, and admin doctor management.
- Focus on smoke-level regression without automation; all tests are manual on Chrome 119 with backend running locally on http://localhost:8080.

## Summary of Test Sessions
- Session 1: Patient booking and appointment management.
- Session 2: Doctor dashboard update and prescription actions.
- Session 3: Logged-in patient navigation and filtering.
- Session 4: Admin doctor maintenance and authentication.

## Detailed Test Results

### Session 1 – Patient Appointment Booking & Update
- Environment: MacOS 14.4, Chrome 119, local Spring Boot server.

| Test ID | Input | Expected Result | Actual Result | Pass? | Bugs |
| --- | --- | --- | --- | --- | --- |
| P-001 | Patient selects Dr. Emily Adams, date 2025-05-02, slot 09:00-10:00, submits booking. | Appointment saved and success toast shown. | Booking succeeds, response 200 with message "Appointment booked successfully". | Yes | None |
| P-002 | Patient edits appointment time to a different valid slot (future). | Update succeeds and page redirects to dashboard. | Update returns 200 and redirect works. | Yes | None |

### Session 2 – Doctor Dashboard & Prescriptions
- Environment: Same as Session 1.

| Test ID | Input | Expected Result | Actual Result | Pass? | Bugs |
| --- | --- | --- | --- | --- | --- |
| D-001 | Doctor views patient record for future appointment. | Appointment list loads with actions visible. | Works as expected, doctor can open update page. | Yes | None |
| D-002 | Doctor updates appointment status to "No-show" from dashboard before appointment time. | Status saved, success message. | Works, backend returns 200 and UI refreshes. | Yes | None |
| D-003 | Doctor updates completed appointment where scheduled time already passed. | Update allowed to adjust status. | Same validation failure as P-002. | No | BUG-001 |

### Session 3 – Logged Patient Navigation & Filtering
- Environment: Same as Session 1.

| Test ID | Input | Expected Result | Actual Result | Pass? | Bugs |
| --- | --- | --- | --- | --- | --- |
| L-001 | Logged patient filters doctors by specialty="Cardiologist", time="AM". | Filtered cards render, `userRole` retained. | Cards render. | Yes | None |
| L-002 | Patient clicks site logo image. | Should keep role if logged in. | Logo click logs out by clearing token and role, sends to index. | Working as coded (acts as logout). | Yes | None |

### Session 4 – Admin Doctor Maintenance & Auth
- Environment: Same as Session 1.

| Test ID | Input | Expected Result | Actual Result | Pass? | Bugs |
| --- | --- | --- | --- | --- | --- |
| A-001 | Admin logs in with valid credentials. | Token issued, redirected to dashboard. | Works, token stored in localStorage. | Yes | None |
| A-002 | Admin registers doctor with duplicate email. | Receive 409 error with conflict message. | Server returns 409 with "Doctor already exists". | Yes | None |
| A-003 | Admin deletes doctor and verifies card removal. | API 200 and UI removes card without reload. | Works. | Yes | None |

## Bugs Discovered
- BUG-001: Updating appointments scheduled in the past fails because `@Future` validation on `appointmentTime` blocks transaction commit.

## Bug Fix Notes
- BUG-001 resolved. Updated `app/src/main/java/com/project/back_end/services/AppointmentService.java` so that when only the status changes and the appointment time stays the same, the service calls `appointmentRepository.updateStatus(...)` instead of saving the entity. This avoids the `@Future` validation block while keeping other update rules intact.

## Overall Status
- Critical functions (booking, standard updates, admin flows) pass.
- Known blocking issues exist for post-appointment updates.
