# Smart Clinic Management System

## Project Structure
- `app/src/main/java`: Spring Boot backend. Handles APIs for appointments, doctors, patients, tokens, and business rules.
- `app/src/main/resources/static`: Vanilla JS frontend. Contains pages, scripts, components, and styles for dashboards and forms.
- `schema-architecture.md`, `schema-design.md`, `user_stories.md`: design notes that explain database layout and product goals.

## Core Features
- Patients can browse doctors, book appointments, and manage existing bookings.
- Doctors can review patient records, update appointment status, and add prescriptions.
- Admins can register or remove doctors and keep doctor schedules current.

## Tech Stack Choices
- **Spring Boot + JPA**: fast to bootstrap REST APIs, integrates smoothly with relational databases.
- **Vanilla JavaScript + Fetch**: lightweight, no build step, easy to deploy with the backend.
- **JWT Authentication**: stateless session handling that works for patients, doctors, and admins.

## What Users Experience
- Launching the backend exposes the API on `http://localhost:8080`.
- Opening the static pages in a browser shows role-based dashboards.
- Users log in, see their data (appointments or records), and trigger actions through simple forms and buttons.

## Notable Highlights
- Reusable UI components for headers, doctor cards, and patient rows keep the frontend consistent.
- Validation helpers prevent double-booking and enforce doctor time slots.
- Status-only updates bypass validation so doctors can close out past visits quickly.

## Development Challenges
- Balancing strict validation with real workflows (e.g., letting doctors mark past visits complete).
- Keeping JWT role checks lightweight while sharing services across controllers.
- Managing navigation state in localStorage so role-based pages always load correctly.

## Known Runtime Issues & Limits
- The app expects the backend and frontend to run from the same origin; cross-origin setups need extra config.
- Browser refreshes on detail pages require the query string parameters to stay intact.
- Manual testing was done on Chrome; other browsers were not fully verified.

## Outstanding Bugs / Missing Features
- No automated test suite yet; all regression checks are manual.
- Password storage is plain text; encryption is not implemented.
- Role-based dashboards do not yet support real-time updates or push notifications.
