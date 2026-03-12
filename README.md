# 🚗 Car Rental System

A full-stack microservices-based car rental platform built with Spring Boot, React, RabbitMQ, Redis, and MongoDB. Users can search rental agencies by city, browse vehicles, make bookings with real-time availability checks, confirm payments, and cancel bookings with automatic refund processing.

---

## 📐 Architecture

```
                        ┌─────────────────┐
                        │   React Frontend │
                        │  (Vite + React)  │
                        └────────┬────────┘
                                 │ HTTP
                        ┌────────▼────────┐
                        │   API Gateway   │  ← JWT validation
                        │   (Port 8090)   │  ← Route forwarding
                        └────────┬────────┘
                                 │
              ┌──────────────────┼──────────────────┐
              │                  │                   │
   ┌──────────▼──────┐ ┌────────▼───────┐ ┌────────▼────────┐
   │  UserService    │ │ AgencyService  │ │ BookingService  │
   │  (Port 8081)    │ │  (Port 8082)   │ │  (Port 8083)    │
   └─────────────────┘ └────────────────┘ └────────┬────────┘
                                                    │
                                           ┌────────▼────────┐
                                           │ PaymentService  │
                                           │  (Port 8085)    │
                                           └─────────────────┘

All services register with Eureka (Port 8761) for service discovery.
BookingService ↔ PaymentService communicate via RabbitMQ.
AgencyService + BookingService use Redis for caching and distributed locking.
```

---

## 🔄 Booking Flow

```
1. User searches agencies by source city
2. Selects agency → browses vehicles
3. Clicks "Book Now"
        → BookingService creates booking (status: PENDING)
        → Redis locks vehicle dates (prevents double booking)
4. User clicks "Confirm" in modal
        → BookingService publishes BookingCreatedEvent to RabbitMQ
        → PaymentService consumes event → processes mock payment
        → On SUCCESS: publishes PaymentSuccessEvent → BookingService sets status: CONFIRMED
        → On FAILURE: publishes PaymentFailedEvent → BookingService sets status: CANCELLED, releases Redis locks
5. Scheduled job runs daily at midnight
        → Marks CONFIRMED bookings past end date as COMPLETED
```

## 🔄 Cancellation & Refund Flow

```
1. User cancels a CONFIRMED booking
        → BookingService publishes BookingCancelledEvent to RabbitMQ
        → PaymentService consumes event → processes mock refund
        → On SUCCESS: publishes RefundSuccessEvent → BookingService sets status: REFUNDED
        → On FAILURE: logs error
2. Frontend polls every 2 seconds for up to 30 seconds
        → Updates UI when status changes to REFUNDED/CANCELLED
```

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, Vite, React Router |
| Backend | Spring Boot 3, Spring Cloud Gateway |
| Service Discovery | Netflix Eureka |
| Messaging | RabbitMQ (with Dead Letter Queues) |
| Cache / Locking | Redis |
| Database | MongoDB |
| Auth | JWT (HMAC-SHA256) |
| Build | Maven |
| Infrastructure | Docker, Docker Compose |

---

## 📁 Project Structure

```
Car-Rental-System/
├── Backend/
│   ├── eureka/              # Service registry (Port 8761)
│   ├── Gateway/             # API Gateway + JWT filter (Port 8090)
│   ├── UserService/         # Auth + user profile (Port 8081)
│   ├── AgencyService/       # Agencies + vehicles + search (Port 8082)
│   ├── BookingService/      # Booking lifecycle + scheduling (Port 8083)
│   ├── PaymentService/      # Mock payment + refund processing (Port 8085)
│   └── docker-compose.yaml  # Full stack orchestration
└── Frontend/
    ├── src/
    │   ├── Pages/           # Login, Register, Home, AgencyPage, VehicleDetails, MyBookings, Profile
    │   ├── Components/      # Header, Sidebar, Footer
    │   ├── Hooks/           # Custom React hooks
    │   └── Services/        # Axios API calls
    └── package.json
```

---

## 🚀 Getting Started

### Prerequisites

- Docker + Docker Compose
- Node.js 18+ (for frontend only)

---

### Step 1 — Clone the repo

```bash
git clone https://github.com/your-username/Car-Rental-System.git
cd Car-Rental-System
```

### Step 2 — Create environment file

Create a `.env` file inside the `Backend/` folder:

```
JWT_SECRET_KEY2=your-secret-key-here
```

### Step 3 — Start the entire backend with one command

```bash
cd Backend
docker-compose up --build
```

This single command builds and starts everything:
- MongoDB, Redis, RabbitMQ (infrastructure)
- Eureka (service registry)
- Gateway, UserService, AgencyService, BookingService, PaymentService

Wait until you see `Started ... in X seconds` in the logs for all services. Verify everything is up at `http://localhost:8761` — all 5 services should show as UP.

### Step 4 — Start the frontend

```bash
cd Frontend
npm install
npm run dev
```

App runs at `http://localhost:5173`

---

### Useful commands

```bash
# Run in background
docker-compose up --build -d

# Stop everything
docker-compose down

# View logs for a specific service
docker-compose logs -f booking-service

# Rebuild a single service after code change
docker-compose up --build booking-service
```

---

### Option B — Cloud / Production

Switch `application.yml` in every service to `active: prod` and set the following environment variables on your deployment platform:

| Variable | Used By | Description |
|---|---|---|
| `MONGODB_URI` | All services | MongoDB Atlas connection URI |
| `REDIS_HOST` | AgencyService, BookingService | Redis Cloud host |
| `REDIS_PORT` | AgencyService, BookingService | Redis Cloud port |
| `REDIS_PASSWORD` | AgencyService, BookingService | Redis Cloud password |
| `RABBITMQ_HOST` | BookingService, PaymentService | RabbitMQ host |
| `RABBITMQ_USERNAME` | BookingService, PaymentService | RabbitMQ username |
| `RABBITMQ_PASSWORD` | BookingService, PaymentService | RabbitMQ password |
| `JWT_SECRET_KEY` | Gateway, UserService | HMAC secret (min 32 chars) |
| `FRONTEND_URL` | Gateway | Frontend origin for CORS |

---

## 🌐 API Endpoints

All requests go through the Gateway at `http://localhost:8090`.
All endpoints except `/api/auth/**` require `Authorization: Bearer <token>` header.

### Auth — `/api/auth`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/signup` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |

### User — `/api/user`
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/user/profile` | Get logged-in user's profile |

### Agency — `/api/agency`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/agency/search` | Search agencies by source city (paginated) |
| GET | `/api/agency/{agencyId}` | Get agency details with vehicles |
| GET | `/api/agency/vehicle/{vehicleId}` | Get vehicle details |
| POST | `/api/agency/register` | Register a new agency |

### Booking — `/api/booking`
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/booking` | Create a booking (status: PENDING) |
| PUT | `/api/booking/confirm/{bookingId}` | Confirm booking → triggers payment |
| PUT | `/api/booking/cancel` | Cancel a confirmed booking → triggers refund |
| GET | `/api/booking/my` | Get all bookings for logged-in user |

---

## 📨 RabbitMQ Exchanges & Queues

### Booking Exchange (`booking.exchange`)
| Queue | Routing Key | Publisher | Consumer |
|---|---|---|---|
| `booking.created.queue` | `booking.created.key` | BookingService | PaymentService |
| `booking.cancelled.queue` | `booking.cancelled.key` | BookingService | PaymentService |

### Payment Exchange (`payment.exchange`)
| Queue | Routing Key | Publisher | Consumer |
|---|---|---|---|
| `payment.success.queue` | `payment.success` | PaymentService | BookingService |
| `payment.failed.queue` | `payment.failed` | PaymentService | BookingService |
| `payment.refund.queue` | `payment.refund` | PaymentService | BookingService |
| `payment.dead.letter.queue` | `payment.dead` | DLX | — |

---

## 🔐 Security

- JWT tokens are validated at the Gateway before forwarding to any service
- Gateway injects `X-User-Id` and `X-Username` headers for downstream services
- All booking operations verify the requesting user owns the booking
- Passwords are BCrypt encoded
- `/api/auth/**` is the only public route — all others require a valid JWT

---

## 🧪 Running Tests

```bash
# BookingService
cd Backend/BookingService && mvn test

# UserService
cd Backend/UserService && mvn test
```

Tests cover service layer (unit tests with Mockito) and controller layer (MockMvc).

---

## 🔧 Service Ports Reference

| Service | Port |
|---|---|
| Eureka Dashboard | 8761 |
| API Gateway | 8090 |
| UserService | 8081 |
| AgencyService | 8082 |
| BookingService | 8083 |
| PaymentService | 8085 |
| MongoDB | 27017 |
| Redis | 6379 |
| RabbitMQ AMQP | 5672 |
| RabbitMQ Management UI | 15672 |
| Frontend | 5173 |

---

## 📌 Known Limitations & Planned Improvements

- Payment is mocked via `MockPaymentProvider` — real payment gateway integration (Razorpay/Stripe) planned
- Cost calculation currently uses `pricePerKm × days` — Google Maps Distance Matrix API integration planned for actual route distance
- No refresh token mechanism — JWT expiry requires re-login
- Search currently filters by source city only — destination city filter planned
- No admin panel for agency management

---

## 👤 Author

Built as a portfolio project demonstrating microservices architecture, event-driven design, and full-stack development.
