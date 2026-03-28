# SkyTrack — Flight Delay Management System

A full-stack system simulating airline operations with real-time flight tracking, automated passenger alerts, and rule-based booking logic.

**[Live Demo](https://flight-delay-frontend-seven.vercel.app)** · **[Backend API](https://github.com/sharmakhushi18/flight-delay-alert-api)**

---

## Core System Behavior

- Flight status updates trigger automatic passenger alert generation
- Booking is restricted using a whitelist-based status validation
- Frontend state auto-refreshes every 30 seconds without UI disruption
- Form workflows include validation, error propagation, and controlled loading states

---

## Impact & Engineering Highlights

| What | Why It Matters |
|------|----------------|
| Auto-refresh every 30s (silent) | Eliminates manual reload, mimics real-time dashboards |
| Whitelist booking logic `["ON_TIME", "DELAYED", "BOARDING"]` | Scalable status control without hardcoding exclusions |
| Separate loading states | Prevents UI conflicts during concurrent actions |
| Service layer (`api.js`) | Centralized API communication |
| `STATUS_META` utility | Single source for status UI mapping |
| Skeleton loading | Improves perceived performance |
| Error propagation (`err.message`) | Backend errors visible to user |

---

## Features

- Live flight status dashboard (On Time, Delayed, Cancelled)
- Add and update flight status (triggers backend alerts)
- Search and filter by flight number, route, and status
- Passenger registration with validation
- Seat booking with eligibility checks
- Passenger alert lookup
- Silent auto-refresh every 30 seconds
- Responsive UI for mobile and desktop

---

## System Design

```
┌─────────────────────────────────────────────────────┐
│                  SkyTrack Frontend                   │
│                                                     │
│  Navbar → Tab routing (Flights / Book / Alerts)     │
│                                                     │
│  FlightList                                         │
│  ├── Stats (live counts from flight state)          │
│  ├── Add Flight       → POST /flights               │
│  ├── Update Status    → PUT /flights/:id/status     │
│  └── Flight Cards (search + filter)                 │
│                                                     │
│  BookingForm                                        │
│  ├── Register Passenger → POST /passengers          │
│  └── Book Flight        → POST /bookings            │
│      └── Whitelist check before API call            │
│                                                     │
│  AlertList                                          │
│  └── GET /alerts/:passengerId                       │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────┐
│           Spring Boot REST API (Backend)             │
│  /flights  /passengers  /bookings  /alerts          │
│  Auto-generates alerts on status change             │
└─────────────────────────────────────────────────────┘
                         │
                         ▼
                   MySQL Database
```

---

## Project Structure

```
src/
├── components/
│   ├── Navbar.js
│   ├── FlightList.js
│   ├── BookingForm.js
│   ├── AlertList.js
│   └── StatusBadge.js
├── services/
│   └── api.js
├── utils/
│   └── helpers.js
├── App.js
└── App.css
```

---

## Tech Stack

| Technology | Usage |
|------------|-------|
| React 18 | Component-based UI |
| JavaScript ES6+ | Application logic |
| CSS3 | Styling and UI |
| Fetch API | REST communication |
| Spring Boot | Backend REST API |
| MySQL | Data storage |
| Vercel | Frontend deployment |
| Render | Backend deployment |

---

## API Endpoints (Sample)

```
GET    /flights
POST   /flights
PUT    /flights/{id}/status
POST   /passengers
POST   /bookings
GET    /alerts/{passengerId}
```

---

## Run Locally

```bash
git clone https://github.com/sharmakhushi18/skytrack-frontend.git
cd skytrack-frontend
npm install
npm start
```

> Backend should run on `http://localhost:8080`

---

## Screenshots

### Flights Dashboard
![Flights](flights-tab.png)

### Book Flight
![Book](book-tab.png)

### Passenger Alerts
![Alerts](alerts-tab.png)

---

## Author

**Khushi Sharma**
Full Stack Developer | Java + React
Final Year ECE · LNCT Bhopal
[github.com/sharmakhushi18](https://github.com/sharmakhushi18)
