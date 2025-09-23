# User Story For Admin

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

**Title** Run monthly appointment stats (stored procedure)

As an admin, I want to run a stored procedure in MySQL CLI to get appointments per month, so that I can track usage statistics.

**Acceptance Criteria:**
1. Returns rows with month and count.
2. Results can be exported or recorded for reporting.
   
**priority** Medium
**Story Points** 2
