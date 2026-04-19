# ✈️ SkyTrack — Flight Delay Alert API

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Deployed-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-Live-46E3B7?style=for-the-badge&logo=render&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-Secured-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

**A real-world backend system that automatically notifies passengers when their flight is delayed or cancelled — with real email alerts.**

[Live Backend](https://flight-delay-alert-api.onrender.com/flights) · [Live Frontend](https://flight-delay-frontend-seven.vercel.app) · [Report Bug](https://github.com/sharmakhushi18/flight-delay-alert-api/issues)

</div>

---

## 🌐 Live Demo

| Service | URL |
|---|---|
| Backend API | https://flight-delay-alert-api.onrender.com/flights |
| Frontend Dashboard | https://flight-delay-frontend-seven.vercel.app |

---

## 📌 What Is This?

A Spring Boot REST API that automatically generates passenger alerts and sends real email notifications when a flight status changes to `DELAYED` or `CANCELLED` — no manual intervention required. Built to simulate how real airline notification systems work internally.

Now secured with **JWT Authentication** — all endpoints are protected and require a valid token.

---

## 💡 Why I Built This

Flight delays affect millions of passengers every day — but most airlines still rely on manual announcements or delayed SMS updates.

I wanted to build a system that **automatically detects status changes and instantly notifies every affected passenger** — no manual work, no delay in communication.

This project taught me how real-world **event-driven systems** work — where one action (status update) triggers a chain of automated responses (alert generation + real email notifications for all booked passengers).

---

## 🚀 What This Project Does

When a flight status changes to `DELAYED` or `CANCELLED`, the system automatically:
1. Generates alert notifications in DB for all booked passengers
2. Sends real email notifications to each passenger instantly

```
Admin updates flight status
        ↓
System detects DELAYED / CANCELLED
        ↓
Alerts auto-generated for all booked passengers
        ↓
Real email sent to each passenger instantly
        ↓
Passengers can check their alerts anytime
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 17+ | Core language |
| Spring Boot 3.5 | Backend framework |
| Spring Security + JWT | Authentication & Authorization |
| Spring Data JPA | Database ORM |
| PostgreSQL (Neon) | Cloud relational database |
| Hibernate | ORM implementation |
| JavaMailSender | Real email notifications |
| Lombok | Boilerplate reduction |
| Maven | Build tool |
| Docker | Containerization |
| Render | Cloud deployment |

---

## 📐 Architecture

```
Client (Postman / Frontend)
        ↓
   JWT Filter              ← Token validation on every request
        ↓
   Controller Layer        ← HTTP request handling
        ↓
   Service Layer           ← Business logic & validation
        ↓
   Repository Layer        ← Database operations (JPA)
        ↓
   PostgreSQL Database     ← Persistent storage (Neon Cloud)
```

**Each layer has a single responsibility:**
- JWT Filter validates token before request reaches controller
- Controller handles HTTP only — no business logic
- Service handles all decisions and rules
- Repository handles all database queries

---

## 🗄️ Database Schema

```
users
├── id, username (unique)
├── password (BCrypt encrypted)
└── role (USER / ADMIN)

flights
├── id, flightNumber (unique), source, destination
├── departureTime, totalSeats, availableSeats
├── status (ON_TIME / DELAYED / BOARDING / CANCELLED / DEPARTED)
└── delayMinutes

passengers
├── id, name, email (unique)
├── phone, passportNumber (unique)

bookings
├── id, seatNumber, status (CONFIRMED / CANCELLED / COMPLETED)
├── bookingTime (auto-set)
├── flight_id (FK), passenger_id (FK)

alert_notifications
├── id, message, triggerStatus, alertTime (auto-set)
├── isRead
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
DEPARTED ──→ ❌ (terminal state)
CANCELLED──→ ❌ (terminal state)
```

Invalid transitions are rejected at the service layer — the system never enters an inconsistent state.

---

## 📡 API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|---|---|---|
| POST | /auth/register | Register a new user |
| POST | /auth/login | Login and get JWT token |

### Flights (Protected — Token Required)
| Method | Endpoint | Description |
|---|---|---|
| POST | /flights | Add a new flight |
| GET | /flights | Get all flights |
| GET | /flights/{id}/status | Get flight status |
| PUT | /flights/{id}/status | Update flight status |

### Passengers (Protected — Token Required)
| Method | Endpoint | Description |
|---|---|---|
| POST | /passengers | Register a passenger |
| GET | /passengers | Get all passengers |

### Bookings (Protected — Token Required)
| Method | Endpoint | Description |
|---|---|---|
| POST | /bookings | Book a flight |
| PUT | /bookings/{id}/cancel | Cancel a booking |
| GET | /bookings/passenger/{id} | Get passenger bookings |

### Alerts (Protected — Token Required)
| Method | Endpoint | Description |
|---|---|---|
| GET | /alerts/{passengerId} | Get passenger alerts |

---

## 📬 Sample API Requests

**Step 1 — Register**
```json
POST /auth/register
{
  "username": "khushi",
  "password": "khushi123"
}
```

**Step 2 — Login (get token)**
```json
POST /auth/login
{
  "username": "khushi",
  "password": "khushi123"
}
```
Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9..."
}
```

**Step 3 — Use token in all requests**
```
Authorization: Bearer <your_token>
```

**Create Flight**
```json
POST /flights
{
  "flightNumber": "AI101",
  "source": "Delhi",
  "destination": "Mumbai",
  "departureTime": "2026-03-10T10:30:00",
  "totalSeats": 100,
  "availableSeats": 100,
  "status": "ON_TIME",
  "delayMinutes": 0
}
```

**Book a Flight**
```json
POST /bookings
{
  "flightId": 1,
  "passengerId": 1,
  "seatNumber": "A1"
}
```

**Update Flight Status — triggers alerts + email automatically**
```json
PUT /flights/1/status
{
  "status": "DELAYED",
  "delayMinutes": 45
}
```

---

## ⚙️ How to Run Locally

**Prerequisites**
- Java 17+
- PostgreSQL or Neon DB account
- Maven

**Steps**

```bash
# 1. Clone the repository
git clone https://github.com/sharmakhushi18/flight-delay-alert-api.git

# 2. Set environment variables in application.properties
SPRING_DATASOURCE_URL=your_postgresql_url
SPRING_DATASOURCE_USERNAME=your_username
SPRING_DATASOURCE_PASSWORD=your_password
SPRING_MAIL_USERNAME=your_gmail
SPRING_MAIL_PASSWORD=your_gmail_app_password
JWT_SECRET=your_secret_key
JWT_EXPIRATION=86400000

# 3. Run the application
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## 💡 Key Design Decisions

**Why JWT Authentication?**
Stateless authentication — no session stored on server. Every request carries a self-contained token with user identity and role. Perfect for REST APIs and scalable deployments.

**Why BCrypt for passwords?**
BCrypt is a one-way hash — passwords are never stored in plain text. Even if the database is compromised, passwords remain secure.

**Why `@Transactional` on status update?**
Status change and alert generation must all succeed together — or rollback together. This ensures data consistency even if the server crashes mid-operation.

**Why Enum for FlightStatus?**
Prevents invalid string values at compile time. The state machine logic becomes clean and readable — invalid transitions are caught before they reach the database.

**Why auto-trigger alerts + email?**
Alerts and emails are side effects of status change — not separate manual steps. This reflects real-world event-driven behavior where one action automatically triggers downstream effects.

**Why try-catch around email sending?**
Email failure should never prevent alert from being saved in DB. Alert persistence is guaranteed even if SMTP fails temporarily.

**Why unique constraints on flightNumber, email, passport?**
Duplicate prevention must happen at the database level — not just the application level. If two requests arrive simultaneously, only the database constraint guarantees one will fail cleanly.

---

## 🔮 Future Improvements

- [x] ~~JWT Authentication — secure all endpoints with role-based access~~ ✅ **Completed**
- [ ] WebSocket Support — push real-time alerts to frontend without polling
- [ ] Pagination — add pagination to all list endpoints for large datasets
- [ ] Swagger UI — interactive API documentation for easier testing
- [ ] Rate Limiting — prevent API abuse with Redis-based request throttling

---

## 📸 API Screenshots

**Create Flight**
![Create Flight](create-flight.png)

**Create Passenger**
![Create Passenger](create-passenger.png)

**Create Booking**
![Create Booking](create-booking.png)

**Get Flights**
![Get Flights](get-flights.png)

**Get Bookings**
![Get Bookings](get-bookings.png)

**Update Flight Status**
![Update Status](update-status.png)

---

## 👩‍💻 Author

**Khushi Sharma**
Full Stack Developer | Java + React
Final Year ECE · LNCT Bhopal

[![GitHub](https://img.shields.io/badge/GitHub-sharmakhushi18-181717?style=flat&logo=github)](https://github.com/sharmakhushi18)
