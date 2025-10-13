# User Stories For Admin

**Title** Admin logs in

As an admin, I want to log into the portal with my username and password, so that I can manage the platform securely.

**Acceptance Criteria:**
1. With valid credentials I am authenticated and redirected to the admin dashboard.
2. With invalid credentials I see an error and no session is created.

**priority** High
**Story Points** 2

**Title** Admin logs out

As an admin, I want to log out of the portal, so that I protect system access when I finish work.

**Acceptance Criteria:**
1. Clicking Logout ends the session and redirects me to the login page.
2. After logout, protected pages are no longer accessible.
   
**priority** High
**Story Points** 1

**Title** Add a doctor

As an admin, I want to add doctors to the portal, so that they can be scheduled and manage patients.

**Acceptance Criteria:**
1. Required fields (name, specialty, email) must be provided; email is unique.
2. On success the new doctor appears in the doctors list.
   
**priority** High
**Story Points** 3

**Title** Delete a doctor profile

As an admin, I want to delete a doctor’s profile from the portal, so that I can remove inactive accounts.

**Acceptance Criteria:**
1. Deletion asks for confirmation and then removes the profile.
2. The doctor no longer shows up in search or scheduling.
   
**priority** Medium
**Story Points** 2

# User Stories For Patient

**Title:** Browse doctors without logging in

As a patient, I want to view a list of doctors without logging in, so that I can explore options before registering.

**Acceptance Criteria:**
1. Open the Doctors page with no session, I see public info (name, specialty, location).
2. Filter/search by specialty or name.
3. Booking actions require sign-up or login.
 
**Priority:** High
**Story Points:** 2

**Title:** Sign up with email and password

As a patient, I want to sign up using my email and password, so that I can book appointments.

**Acceptance Criteria:**
1. With valid inputs, an account is created and I’m redirected to the patient dashboard.
2. Email must be unique; duplicate emails show a clear error.
3. Password must meet policy (≥8 chars) and match confirmation.

**Priority:** High  **Story Points:** 3

**Notes:** Verify email format; mask password fields.

**Title:** Log in to manage bookings

As a patient, I want to log into the portal, so that I can view and manage my bookings.

**Acceptance Criteria:**
1. With valid credentials, I’m authenticated and taken to “My Appointments”.
2. With invalid credentials, see an error and remain logged out.
3. After login, access actions like reschedule/cancel where permitted.

**Priority:** High  **Story Points:** 2

**Title:** Log out to secure account

As a patient, I want to log out of the portal, so that I can secure my account.

**Acceptance Criteria:**
1. Click Logout, session ends and redirected to the login page.
2. After logout, any attempt to open a protected page sends me to the login page.
3. Session cookies/tokens are invalidated on the server and cleared from the browser.

**Priority:** High  **Story Points:** 1

**Notes:** Show a visible Logout button in the header; consider auto-logout after inactivity.

**Title:** Book a appointment

As a patient, I want to log in and book an hour-long appointment with a doctor, so that I can consult properly.

**Acceptance Criteria:**
1. Select a doctor and an available 60-minute slot and confirm.
2. Conflicting or unavailable slots are blocked with a message.
3. On success, the appointment is saved and a confirmation is shown.

**Priority:** High  **Story Points:** 3

**Notes:** Store duration = 60 minutes.

**Title:** View upcoming appointments

As a patient, I want to view my upcoming appointments, so that I can prepare accordingly.

**Acceptance Criteria:**
1. The dashboard lists future appointments sorted by date/time.
2. Each entry shows doctor, date/time, and location/mode.
3. Past appointments are excluded from the “Upcoming” list.

**Priority:** Medium  **Story Points:** 2

# User Stories For Doctor

**Title:** Doctor logs in

As a doctor, I want to log into the portal, so that I can manage my appointments.

**Acceptance Criteria:**
1. With valid credentials I am authenticated and redirected to the doctor dashboard.
2. With invalid credentials see an error and remain logged out.

**Priority:** High  **Story Points:** 2

**Title:** Doctor logs out

As a doctor, I want to log out of the portal, so that I can protect my data.

**Acceptance Criteria:**

1. Clicking Logout ends the session and redirects to the login page.
2. Protected pages are inaccessible until I sign in again.
3. Tokens/cookies are invalidated on the server.

**Priority:** High  **Story Points:** 1

**Title:** View my appointment calendar

As a doctor, I want to view my appointment calendar, so that I can stay organized.

**Acceptance Criteria:**
1. See my appointments in day/week views sorted by time.
2. Filter by date range and appointment status.
3. Only my own appointments are shown.

**Priority:** High  **Story Points:** 3

**Title:** Mark unavailability

As a doctor, I want to mark time ranges when I am unavailable, so that patients only see available slots.

**Acceptance Criteria:**
1. Add/edit/delete unavailability blocks with date and time.
2. If a block overlaps existing bookings, I am warned and asked to reschedule or skip.

**Priority:** High  **Story Points:** 5

**Title:** View patient details for upcoming appointments

As a doctor, I want to view key patient details for upcoming appointments, so that I can prepare.

**Acceptance Criteria:**
1. For each upcoming appointment I can see patient name, time, and visit reason.
2. I can open a read-only snapshot of recent notes/prescriptions where permitted.
3. Access respects role-based permissions; PHI is visible only to assigned doctors.

**Priority:** High  **Story Points:** 3


