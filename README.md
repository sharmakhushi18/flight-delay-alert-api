# ✈️ Flight Delay Alert API

A **real-world backend system** built with Spring Boot that automatically notifies passengers when their flight is delayed or cancelled.

> Built by **Khushi Sharma** | Java Backend Developer | LNCT Bhopal

---

## 🚀 What This Project Does

When a flight status changes to **DELAYED** or **CANCELLED**, the system automatically generates alert notifications for all passengers who have booked that flight — without any manual intervention.

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
|------------|-------|
| Java 17+ | Core language |
| Spring Boot 3.5 | Backend framework |
| Spring Data JPA | Database ORM |
| MySQL 8.0 | Relational database |
| Hibernate | ORM implementation |
| Lombok | Boilerplate reduction |
| Maven | Build tool |

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
ON_TIME ──→ BOARDING
ON_TIME ──→ DELAYED
ON_TIME ──→ CANCELLED
DELAYED ──→ BOARDING
DELAYED ──→ CANCELLED
BOARDING ──→ DEPARTED
DEPARTED ──→ ❌ (no transition)
CANCELLED ──→ ❌ (no transition)
```

Invalid transitions are **rejected at service layer**.

---

## 📡 API Endpoints

### Flights
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/flights` | Add a new flight |
| GET | `/flights` | Get all flights |
| GET | `/flights/{id}/status` | Get flight status |
| PUT | `/flights/{id}/status` | Update flight status |

### Passengers
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/passengers` | Register a passenger |
| GET | `/passengers` | Get all passengers |

### Bookings
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/bookings` | Book a flight |
| PUT | `/bookings/{id}/cancel` | Cancel a booking |
| GET | `/bookings/passenger/{id}` | Get passenger bookings |

### Alerts
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/alerts/subscribe` | Subscribe for alerts |
| GET | `/alerts/{passengerId}` | Get passenger alerts |

---

## 📬 Sample API Requests

### Create Flight
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

### Book a Flight
```json
POST /bookings
{
  "flightId": 1,
  "passengerId": 1,
  "seatNumber": "A1"
}
```

### Update Flight Status (triggers alerts automatically)
```json
PUT /flights/1/status
{
  "status": "DELAYED",
  "delayMinutes": 45
}
```

---

## ⚙️ How to Run Locally

### Prerequisites
- Java 17+
- MySQL 8.0
- Maven

### Steps

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
Seat decrement, status change, and alert generation must succeed together or rollback together.

**Why Enum for FlightStatus?**
Prevents invalid string values and enables compile-time safety for state transitions.

**Why auto-trigger alerts?**
Alerts are side effects of status change — not separate API calls. This reflects real-world event-driven behavior.

**Why unique constraints on flightNumber, email, passport?**
Prevents duplicate data at database level, not just application level.

---
## 📸 API Screenshots

### Create Flight
![Create Flight](create-flight.png)

### Create Passenger
![Create Passenger](create-passenger.png)

### Create Booking
![Create Booking](create-booking.png)

### Get Flights
![Get Flights](get-flights.png)

### Get Bookings
![Get Bookings](get-bookings.png)

### Update Flight Status
![Update Status](update-status.png)

---

## 👩‍💻 Author

**Khushi Sharma**
- GitHub: [@sharmakhushi18](https://github.com/sharmakhushi18)
- Final Year ECE | LNCT Bhopal


