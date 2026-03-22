# ✈️ Flight Delay Alert API

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Deployed-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![Render](https://img.shields.io/badge/Render-Live-46E3B7?style=for-the-badge&logo=render&logoColor=white)

**A real-world backend system that automatically notifies passengers when their flight is delayed or cancelled — without any manual intervention.**

[Live Backend](https://flight-delay-alert-api.onrender.com) · [Live Frontend](https://flight-delay-frontend-seven.vercel.app) · [Report Bug](https://github.com/sharmakhushi18/flight-delay-alert-api/issues)

</div>

---

## 🌐 Live Demo

| Service | URL |
|---|---|
| Backend API | https://flight-delay-alert-api.onrender.com |
| Frontend Dashboard | https://flight-delay-frontend-seven.vercel.app |

---

## 📌 What Is This?

A Spring Boot REST API that automatically generates passenger alerts when a flight status changes to `DELAYED` or `CANCELLED` — no manual intervention required. Built to simulate how real airline notification systems work internally.

---

## 💡 Why I Built This

Flight delays affect millions of passengers every day — but most airlines still rely on manual announcements or delayed SMS updates.

I wanted to build a system that **automatically detects status changes and instantly notifies every affected passenger** — no manual work, no delay in communication.

This project taught me how real-world **event-driven systems** work — where one action (status update) triggers a chain of automated responses (alert generation for all booked passengers).

---

## 🚀 What This Project Does

When a flight status changes to `DELAYED` or `CANCELLED`, the system automatically generates alert notifications for all passengers who have booked that flight.

```
Admin updates flight status
        ↓
System detects DELAYED / CANCELLED
        ↓
Alerts auto-generated for all booked passengers
        ↓
Passengers can check their alerts anytime
```

---

## 🛠️ Tech Stack

| Technology | Usage |
|---|---|
| Java 17+ | Core language |
| Spring Boot 3.5 | Backend framework |
| Spring Data JPA | Database ORM |
| MySQL 8.0 | Relational database |
| Hibernate | ORM implementation |
| Lombok | Boilerplate reduction |
| Maven | Build tool |
| Docker | Containerization |
| Render | Cloud deployment |

---

## 📐 Architecture

```
Client (Postman / Frontend)
        ↓
   Controller Layer       ← HTTP request handling
        ↓
   Service Layer          ← Business logic & validation
        ↓
   Repository Layer       ← Database operations (JPA)
        ↓
   MySQL Database         ← Persistent storage
```

**Each layer has a single responsibility:**
- Controller handles HTTP only — no business logic
- Service handles all decisions and rules
- Repository handles all database queries

---

## 🗄️ Database Schema

```
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

### Flights
| Method | Endpoint | Description |
|---|---|---|
| POST | /flights | Add a new flight |
| GET | /flights | Get all flights |
| GET | /flights/{id}/status | Get flight status |
| PUT | /flights/{id}/status | Update flight status |

### Passengers
| Method | Endpoint | Description |
|---|---|---|
| POST | /passengers | Register a passenger |
| GET | /passengers | Get all passengers |

### Bookings
| Method | Endpoint | Description |
|---|---|---|
| POST | /bookings | Book a flight |
| PUT | /bookings/{id}/cancel | Cancel a booking |
| GET | /bookings/passenger/{id} | Get passenger bookings |

### Alerts
| Method | Endpoint | Description |
|---|---|---|
| POST | /alerts/subscribe | Subscribe for alerts |
| GET | /alerts/{passengerId} | Get passenger alerts |

---

## 📬 Sample API Requests

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

**Update Flight Status — triggers alerts automatically**
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
- MySQL 8.0
- Maven

**Steps**

```bash
# 1. Clone the repository
git clone https://github.com/sharmakhushi18/flight-delay-alert-api.git

# 2. Create MySQL database
mysql -u root -p
CREATE DATABASE flight_delay_db;

# 3. Update application.properties
spring.datasource.username=root
spring.datasource.password=your_password

# 4. Run the application
mvn spring-boot:run
```

Server starts at: `http://localhost:8080`

---

## 💡 Key Design Decisions

**Why `@Transactional` on status update?**
Seat decrement, status change, and alert generation must all succeed together — or all rollback together. This ensures data consistency even if the server crashes mid-operation.

**Why Enum for FlightStatus?**
Prevents invalid string values at compile time. The state machine logic becomes clean and readable — invalid transitions are caught before they reach the database.

**Why auto-trigger alerts?**
Alerts are a side effect of status change — not a separate manual step. This reflects real-world event-driven behavior where one action automatically triggers downstream effects.

**Why unique constraints on flightNumber, email, passport?**
Duplicate prevention must happen at the database level — not just the application level. If two requests arrive simultaneously, only the database constraint guarantees one will fail cleanly.

---

## 🔮 Future Improvements

- [ ] JWT Authentication — secure all endpoints with role-based access (Admin / Passenger)
- [ ] Real Email Notifications — send actual emails via JavaMailSender when alerts are generated
- [ ] WebSocket Support — push real-time alerts to frontend without polling
- [ ] Pagination — add pagination to all list endpoints for large datasets
- [ ] Swagger UI — interactive API documentation for easier testing
- [ ] Rate Limiting — prevent API abuse with Redis-based request throttling

---

## 👩‍💻 Author

**Khushi Sharma**

Final Year ECE | LNCT Bhopal | Java Backend Developer

[![GitHub](https://img.shields.io/badge/GitHub-sharmakhushi18-181717?style=flat&logo=github)](https://github.com/sharmakhushi18)
