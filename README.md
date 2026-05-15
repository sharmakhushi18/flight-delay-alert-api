# ✈️ SkyTrack — Flight Delay Alert API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-blue)
![JWT](https://img.shields.io/badge/JWT-Auth-red)
![Docker](https://img.shields.io/badge/Docker-Deployed-blue)
![Swagger](https://img.shields.io/badge/Swagger-UI-85EA2D)

Event-driven Spring Boot API — flight delayed? Every booked passenger gets a real email automatically.

[Live Frontend](https://flight-delay-frontend-seven.vercel.app) · [Swagger UI](https://flight-delay-alert-api.onrender.com/swagger-ui/index.html) · [Backend API](https://flight-delay-alert-api.onrender.com/flights)

---

## 🌐 Live

| Service | URL |
|---------|-----|
| Frontend Dashboard | https://flight-delay-frontend-seven.vercel.app |
| Backend API | https://flight-delay-alert-api.onrender.com/flights |
| Swagger UI | https://flight-delay-alert-api.onrender.com/swagger-ui/index.html |

> ⚠️ Hosted on Render free tier — first request may take 30–50s to cold start.

---

## 📌 What Is This?

A Spring Boot REST API that automatically generates passenger alerts and sends real email notifications when a flight status changes to DELAYED or CANCELLED — no manual intervention required.

Core engineering focus: **event-driven consistency** — status update, alert generation, and email dispatch must behave correctly under concurrency, partial failures, and invalid state transitions.

Secured with JWT Authentication. All write endpoints are protected. All APIs documented via Swagger UI — testable directly in the browser.

---

## 🚀 How It Works

```
Admin updates flight status → DELAYED / CANCELLED
        ↓
State machine validates transition at service layer
(invalid transition? rejected before any DB write)
        ↓
@Transactional block — status update + alert generation atomically
(one fails? both roll back)
        ↓
Email dispatch in try-catch — SMTP failure never blocks DB write
        ↓
Passengers receive real Gmail notification instantly
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|------------|-------|
| Java 17 | Core language |
| Spring Boot 3.5 | Backend framework |
| Spring Security + JWT | Stateless auth & role-based access |
| Spring Data JPA + Hibernate | ORM |
| PostgreSQL (Neon Cloud) | Relational database |
| JavaMailSender | Real Gmail SMTP notifications |
| Springdoc OpenAPI (Swagger) | Interactive API documentation |
| Docker | Containerization |
| Render | Cloud deployment |

---

## 📐 Architecture

```
Client (Browser / Postman)
        ↓
   JWT Filter              ← Token validated on every protected request
        ↓
   Controller Layer        ← HTTP routing only, zero business logic
        ↓
   Service Layer           ← State machine, @Transactional, alert logic
        ↓
   Repository Layer        ← Spring Data JPA — DB operations
        ↓
   PostgreSQL (Neon)       ← Persistent storage
```

**Event flow on status change:**
```
PUT /flights/{id}/status
        ↓
State machine validates: is DELAYED → BOARDING a valid transition?
        ↓ (valid)
@Transactional: update flight status + generate alerts for all passengers
        ↓
try-catch: send Gmail email per passenger (SMTP failure isolated)
```

---

## 🗄️ Database Schema

```
users
├── id, username (unique)
├── password (BCrypt hashed)
└── role (USER / ADMIN)

flights
├── id, flightNumber (unique), source, destination
├── departureTime, totalSeats, availableSeats
├── status (ON_TIME / DELAYED / BOARDING / CANCELLED / DEPARTED)
└── delayMinutes

passengers
├── id, name, email (unique)
└── phone, passportNumber (unique)

bookings
├── id, seatNumber, bookingTime (auto-set)
├── status (CONFIRMED / CANCELLED / COMPLETED)
├── flight_id (FK), passenger_id (FK)
└── UNIQUE(flight_id, seatNumber) ← prevents double booking at DB level

alert_notifications
├── id, message, triggerStatus
├── alertTime (auto-set), isRead
├── flight_id (FK), passenger_id (FK)
```

---

## 🔄 Flight Status — State Machine

```
ON_TIME  ──→ BOARDING
ON_TIME  ──→ DELAYED
ON_TIME  ──→ CANCELLED
DELAYED  ──→ BOARDING
DELAYED  ──→ CANCELLED
BOARDING ──→ DEPARTED
DEPARTED ──→ ❌ terminal
CANCELLED──→ ❌ terminal
```

Invalid transitions are rejected at the service layer before any DB write — the system never enters an inconsistent state.

---

## 📡 API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /auth/register | Register a new user |
| POST | /auth/login | Login — returns JWT token |

### Flights
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | /flights | Public | Get all flights |
| GET | /flights/{id}/status | Public | Get flight status |
| POST | /flights | 🔒 Token | Add a new flight |
| PUT | /flights/{id}/status | 🔒 Token | Update status — triggers alerts + email |

### Passengers
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | /passengers | 🔒 Token | Get all passengers |
| POST | /passengers | 🔒 Token | Register a passenger |

### Bookings
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | /bookings | 🔒 Token | Book a flight |
| PUT | /bookings/{id}/cancel | 🔒 Token | Cancel a booking |
| GET | /bookings/passenger/{id} | 🔒 Token | Get passenger bookings |

### Alerts
| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| GET | /alerts/{passengerId} | 🔒 Token | Get passenger alerts |

> All protected endpoints require: `Authorization: Bearer <token>`
> Note: Alert lookup is scoped per passenger ID — production hardening would add ownership validation so passengers can only read their own alerts.

---

## 📬 Sample Requests

### Register & Login
```json
POST /auth/register
{ "username": "khushi", "password": "khushi123" }

POST /auth/login
{ "username": "khushi", "password": "khushi123" }
// Response: { "token": "eyJhbGci..." }
```

### Update Flight Status — triggers alerts + email
```json
PUT /flights/1/status
Authorization: Bearer eyJhbGci...

{
  "status": "DELAYED",
  "delayMinutes": 45
}
```

---

## ⚙️ How to Run Locally

**Prerequisites:** Java 17+, PostgreSQL or Neon DB, Maven

```bash
# 1. Clone
git clone https://github.com/sharmakhushi18/flight-delay-alert-api.git

# 2. Set environment variables in application.properties
SPRING_DATASOURCE_URL=your_postgresql_url
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
SPRING_MAIL_USERNAME=your_gmail
SPRING_MAIL_PASSWORD=your_gmail_app_password
JWT_SECRET=your_secret_key_min_32_chars
JWT_EXPIRATION=86400000

# 3. Run
mvn spring-boot:run
# Server: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui/index.html
```

---

## 💡 Key Design Decisions

**Why JWT?**
Stateless auth — no server-side session. Token carries identity and role. Scales horizontally without sticky sessions.

**Why pessimistic locking on seat booking?**
Two users booking the same seat simultaneously would both see it available and both succeed — causing double booking. Pessimistic lock ensures only one transaction proceeds at a time. DB unique constraint on `(flight_id, seatNumber)` is the final safety net — application-level checks have race conditions, only the database guarantees correctness.

**Why @Transactional on status update?**
Status change and alert generation must succeed or fail together. Partial state — status updated but no alerts generated — is never acceptable. `@Transactional` rolls back both if either fails.

**Why try-catch around email?**
Email delivery is not guaranteed — SMTP can fail. Alert persistence to DB must succeed regardless. The alert record is the source of truth; email is a side effect. Isolating email in try-catch prevents an SMTP timeout from rolling back the entire status update.

**Why state machine for FlightStatus?**
Prevents invalid transitions at runtime. A DEPARTED flight can never be moved back to ON_TIME. Enum-based state machine makes valid transitions explicit, auditable, and testable.

**Why DB-level unique constraints?**
Application-level uniqueness checks have race conditions — two simultaneous requests can both pass the check before either commits. Only the database constraint guarantees correctness under concurrency.

**Why BCrypt?**
Passwords are never stored in plain text. BCrypt is intentionally slow — making brute force attacks computationally expensive even if the DB is compromised.

---

## ✅ Features

- [x] JWT Authentication + Spring Security
- [x] Event-driven email alerts on status change
- [x] Pessimistic locking — concurrent booking safety
- [x] DB unique constraint on `(flight_id, seatNumber)`
- [x] State machine — invalid transitions rejected at service layer
- [x] `@Transactional` — atomic status + alert creation
- [x] Email isolated in try-catch — SMTP failure never blocks DB write
- [x] Swagger UI — all endpoints documented and testable
- [x] Docker + Render deployment
- [x] Input validation with Bean Validation API

---

## 🔮 Roadmap

- WebSocket — real-time push alerts without polling
- Kafka — async alert processing for high-volume flights
- Redis rate limiting — prevent status update spam
- Pagination — for large flight/passenger datasets
- Ownership check on alerts — passengers read only their own

---

## 📸 Screenshots

| Create Flight | Create Passenger |
|---|---|
| ![Create Flight](create-flight.png) | ![Create Passenger](create-passenger.png) |

| Create Booking | Get Bookings |
|---|---|
| ![Create Booking](create-booking.png) | ![Get Bookings](get-bookings.png) |

| Get Flights | Update Status |
|---|---|
| ![Get Flights](get-flights.png) | ![Update Status](update-status.png) |

---

## 👩‍💻 Author

**Khushi Sharma** — Java Backend Developer
Spring Boot · PostgreSQL · JWT · Docker · Event-driven Systems
Final Year ECE @ LNCT Bhopal

[GitHub](https://github.com/sharmakhushi18) · [LinkedIn](https://www.linkedin.com/in/khushi-sharma-523153259/)
