# Expense Tracker — Spring Boot + MySQL

Converted from Node.js/Express + MongoDB to **Java 17 + Spring Boot 3.2 + MySQL**.

---

## Tech Stack

| Layer        | Technology                          |
|-------------|--------------------------------------|
| Language     | Java 17                             |
| Framework    | Spring Boot 3.2                     |
| Security     | Spring Security + JWT (jjwt 0.12)   |
| Database     | MySQL 8+                            |
| ORM          | Spring Data JPA / Hibernate         |
| Validation   | Jakarta Bean Validation             |
| Build        | Maven                               |
| Utilities    | Lombok                              |

---

## Project Structure

```
src/main/java/com/expensetracker/
├── ExpenseTrackerApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── ExpenseController.java
│   └── HealthController.java
├── dto/                          (20+ DTO classes)
├── entity/
│   ├── User.java
│   └── Expense.java
├── exception/
│   ├── BadRequestException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   ├── ExpenseRepository.java
│   └── UserRepository.java
├── security/
│   ├── JwtAuthFilter.java
│   ├── JwtUtils.java
│   └── UserDetailsServiceImpl.java
└── service/
    ├── AuthService.java
    ├── DashboardService.java
    └── ExpenseService.java
```

---

## Setup

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

### 2. Create the database
```sql
CREATE DATABASE expense_tracker;
```

### 3. Configure `application.properties`
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

app.jwt.secret=your_long_secret_key_at_least_32_chars
app.cors.allowed-origins=http://localhost:3000
```

> ⚠️ Make sure `app.jwt.secret` is at least 32 characters for HMAC-SHA256.

### 4. Run
```bash
mvn spring-boot:run
```

The server starts on **port 5000** (same as the original Node.js app).

Hibernate will auto-create the `users` and `expenses` tables on first run (`ddl-auto=update`).

---

## API Endpoints

All endpoints are identical to the original Node.js app. Frontend code requires **no changes**.

### Auth — `/api/auth`
| Method | Path                    | Auth | Description          |
|--------|-------------------------|------|----------------------|
| POST   | `/register`             | No   | Register user        |
| POST   | `/login`                | No   | Login                |
| GET    | `/me`                   | ✅   | Get current user     |
| PUT    | `/update`               | ✅   | Update profile       |
| PUT    | `/change-password`      | ✅   | Change password      |

### Expenses — `/api/expenses`
| Method | Path                              | Auth | Description           |
|--------|-----------------------------------|------|-----------------------|
| GET    | `/`                               | ✅   | List with filters     |
| POST   | `/`                               | ✅   | Create expense        |
| GET    | `/:id`                            | ✅   | Get single expense    |
| PUT    | `/:id`                            | ✅   | Update expense        |
| DELETE | `/:id`                            | ✅   | Delete expense        |
| GET    | `/summary/monthly/:year/:month`   | ✅   | Monthly summary       |
| GET    | `/summary/yearly/:year`           | ✅   | Yearly summary        |

### Dashboard — `/api/dashboard`
| Method | Path           | Auth | Description              |
|--------|----------------|------|--------------------------|
| GET    | `/stats`       | ✅   | Dashboard statistics     |
| GET    | `/suggestions` | ✅   | Spending suggestions     |

### Health
| Method | Path          | Auth | Description |
|--------|---------------|------|-------------|
| GET    | `/api/health` | No   | Health check |

---

## Authentication

All protected endpoints require the header:
```
Authorization: Bearer <token>
```

Tokens are returned by `/register` and `/login`.

---

## Key Differences from Node.js Version

| Aspect        | Node.js (original)         | Spring Boot (this)             |
|---------------|----------------------------|--------------------------------|
| Database      | MongoDB (mongoose)         | MySQL (JPA/Hibernate)          |
| IDs           | MongoDB ObjectId (string)  | Auto-increment Long (integer)  |
| Password hash | bcryptjs                   | BCryptPasswordEncoder          |
| JWT           | jsonwebtoken               | jjwt 0.12                      |
| Validation    | express-validator          | Jakarta Bean Validation        |
| Tags field    | Array in MongoDB           | Comma-separated string in MySQL|

---

## Building for Production

```bash
mvn clean package -DskipTests
java -jar target/expense-tracker-1.0.0.jar
```

For production, set:
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
logging.level.com.expensetracker=INFO
```
