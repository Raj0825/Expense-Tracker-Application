# 💸 SpendSmart — Expense Tracker

> A full-stack personal finance tracker built with **Spring Boot** + **MySQL** backend and a modern dark-themed HTML/CSS/JS frontend. Track expenses, visualize spending by category, manage budgets, and get smart financial insights.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=flat-square&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JWT](https://img.shields.io/badge/Auth-JWT-yellow?style=flat-square)

---

🌐 Live Demo

URL🖥️ Frontendhttps://raj0825.github.io/Expense-Tracker-Application-Frontend/login.html
⚙️ Backend APIhttps://spendsmart-api-ooqh.onrender.com/api/health
🗄️ DatabaseAiven Cloud MySQL (Bangalore)

⚠️ Note: Backend is on Render free tier — first request may take 30-60 seconds to wake up.

---

## ✨ Features

- 🔐 **JWT Authentication** — Secure register/login with token-based auth
- 📊 **Dashboard** — Overview of this month, last month, total spent, budget remaining
- 📈 **Last 7 Days Bar Chart** — Visual spending trend for the past week
- 🍩 **By Category Donut Chart** — Interactive pie chart showing spending breakdown
- 💳 **Expense Management** — Add, edit, delete, search, filter, and paginate expenses
- 📁 **11 Categories** — Food & Dining, Travel, Clothes, Bills, Entertainment, Health, Education, Shopping, Housing, Personal Care, Other
- 💰 **Payment Methods** — Cash, Card, UPI, Net Banking, Other
- 📅 **Monthly Reports** — Month-by-month spending summaries
- 💡 **Smart Suggestions** — AI-powered spending insights and alerts
- 👤 **Profile Management** — Update name, email, currency, monthly budget
- 🔄 **Recurring Expenses** — Mark expenses as daily/weekly/monthly/yearly recurring
- 🏷️ **Tags** — Add custom tags to any expense
- 📱 **Responsive Design** — Works on desktop and mobile

---

## 🛠️ Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Java 17 | Core language |
| Spring Boot 3.2.0 | Application framework |
| Spring Security | Authentication & authorization |
| Spring Data JPA | Database ORM |
| MySQL 8.0 | Relational database |
| JWT (jjwt 0.12.3) | Token-based auth |
| Lombok | Boilerplate reduction |
| Bean Validation | Request validation |

### Frontend
| Technology | Purpose |
|---|---|
| HTML5 / CSS3 | Structure and styling |
| Vanilla JavaScript | Interactivity and API calls |
| Google Fonts (Syne + DM Sans) | Typography |
| SVG | Donut/pie chart rendering |
| CSS Variables | Theming system |

---

## 📁 Project Structure

```
expense-tracker-springboot/
├── src/
│   ├── main/
│   │   ├── java/com/expensetracker/
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Spring Security + CORS config
│   │   │   ├── controller/
│   │   │   │   ├── AuthController.java           # Register, Login, Profile endpoints
│   │   │   │   ├── ExpenseController.java        # CRUD expense endpoints
│   │   │   │   ├── DashboardController.java      # Stats and suggestions
│   │   │   │   └── HealthController.java         # Health check endpoint
│   │   │   ├── dto/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   ├── RegisterRequest.java
│   │   │   │   ├── AuthResponse.java
│   │   │   │   ├── ExpenseRequest.java
│   │   │   │   ├── ExpenseResponse.java
│   │   │   │   ├── DashboardStatsResponse.java
│   │   │   │   └── ...more DTOs
│   │   │   ├── entity/
│   │   │   │   ├── User.java                    # User entity
│   │   │   │   └── Expense.java                 # Expense entity
│   │   │   ├── exception/
│   │   │   │   ├── GlobalExceptionHandler.java  # Centralized error handling
│   │   │   │   ├── BadRequestException.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   ├── UserRepository.java
│   │   │   │   └── ExpenseRepository.java
│   │   │   ├── security/
│   │   │   │   ├── JwtUtils.java                # JWT generation & validation
│   │   │   │   ├── JwtAuthFilter.java           # JWT request filter
│   │   │   │   └── UserDetailsServiceImpl.java
│   │   │   ├── service/
│   │   │   │   ├── AuthService.java             # Register, login, profile logic
│   │   │   │   ├── ExpenseService.java          # Expense business logic
│   │   │   │   └── DashboardService.java        # Stats calculation logic
│   │   │   └── ExpenseTrackerApplication.java   # Main entry point
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── index.html                   # Redirect to login
│   │       │   ├── login.html                   # Login & Register page
│   │       │   ├── dashboard.html               # Main dashboard
│   │       │   ├── expenses.html                # Expenses list
│   │       │   ├── add-expense.html             # Add/Edit expense form
│   │       │   ├── reports.html                 # Monthly reports
│   │       │   ├── suggestions.html             # Smart suggestions
│   │       │   ├── profile.html                 # User profile/settings
│   │       │   ├── shared.css                   # Global styles
│   │       │   └── shared.js                    # Shared utilities & API helper
│   │       └── application.properties           # App configuration
├── pom.xml                                      # Maven dependencies
└── README.md
```

---

## ⚙️ Prerequisites

Make sure you have these installed:

- **Java 17+** — [Download](https://adoptium.net/)
- **MySQL 8.0+** — [Download](https://dev.mysql.com/downloads/)
- **IntelliJ IDEA** (recommended) — [Download](https://www.jetbrains.com/idea/)
- **Git** — [Download](https://git-scm.com/)

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/expense-tracker-springboot.git
cd expense-tracker-springboot
```

### 2. Set Up MySQL Database

Open MySQL and run:
```sql
CREATE DATABASE expense_tracker;
```
> The app will auto-create tables on first run via `spring.jpa.hibernate.ddl-auto=update`

### 3. Configure application.properties

Edit `src/main/resources/application.properties`:

```properties
# Change port if needed (default MySQL is 3306, XAMPP uses 3307)
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=yourpassword

# Change JWT secret to something secure in production
app.jwt.secret=your_super_secret_key_here
```

### 4. Run the Application

**Option A — IntelliJ IDEA:**
1. Open the project in IntelliJ
2. Wait for Maven to download dependencies
3. Click the green **Run** button on `ExpenseTrackerApplication.java`

**Option B — Maven (if installed):**
```bash
mvn clean spring-boot:run
```

### 5. Open in Browser

```
http://localhost:8080
```
You'll be redirected to the login page automatically.

---

## 🔑 API Endpoints

### Auth
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login and get JWT token | No |
| GET | `/api/auth/me` | Get current user profile | Yes |
| PUT | `/api/auth/update` | Update profile | Yes |
| PUT | `/api/auth/change-password` | Change password | Yes |

### Expenses
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/expenses` | Get all expenses (paginated) | Yes |
| POST | `/api/expenses` | Create new expense | Yes |
| GET | `/api/expenses/{id}` | Get single expense | Yes |
| PUT | `/api/expenses/{id}` | Update expense | Yes |
| DELETE | `/api/expenses/{id}` | Delete expense | Yes |

### Dashboard
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| GET | `/api/dashboard/stats` | Get dashboard statistics | Yes |
| GET | `/api/dashboard/suggestions` | Get smart suggestions | Yes |

### Query Parameters for GET /api/expenses
```
?page=1&limit=15&category=food&search=lunch&startDate=2026-01-01&endDate=2026-12-31
```

---

## 🔐 Authentication Flow

```
1. User registers → POST /api/auth/register
2. User logs in  → POST /api/auth/login → receives JWT token
3. Token stored  → localStorage
4. Every request → Authorization: Bearer <token> header
5. Token expires → 30 days (configurable in application.properties)
```

---

## 💡 Expense Categories

| Icon | Category | Icon | Category |
|------|----------|------|----------|
| 🍔 | Food & Dining | 🎬 | Entertainment |
| ✈️ | Travel | 💊 | Health & Fitness |
| 👗 | Clothes | 📚 | Education |
| ⚡ | Bills & Utilities | 🛍️ | Shopping |
| 🏠 | Housing & Rent | 💆 | Personal Care |
| 📦 | Other | | |

---

## 🗄️ Database Schema

### users table
```sql
CREATE TABLE users (
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    currency    VARCHAR(10) DEFAULT 'INR',
    monthly_budget DECIMAL(10,2) DEFAULT 0,
    created_at  DATETIME
);
```

### expenses table
```sql
CREATE TABLE expenses (
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id              BIGINT NOT NULL,
    title                VARCHAR(255) NOT NULL,
    amount               DECIMAL(10,2) NOT NULL,
    category             VARCHAR(50) NOT NULL,
    payment_method       VARCHAR(50),
    description          TEXT,
    date                 DATETIME NOT NULL,
    is_recurring         BOOLEAN DEFAULT FALSE,
    recurring_frequency  VARCHAR(20),
    tags                 TEXT,
    created_at           DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

---

## 🌐 Environment Variables (Production)

For production deployment, replace hardcoded values with environment variables:

```properties
spring.datasource.url=${DATABASE_URL}
spring.datasource.username=${DATABASE_USER}
spring.datasource.password=${DATABASE_PASSWORD}
app.jwt.secret=${JWT_SECRET}
app.cors.allowed-origins=${FRONTEND_URL}
```

---

## 🐛 Common Issues & Fixes

### 404 on frontend pages
Add to `application.properties`:
```properties
spring.web.resources.static-locations=classpath:/static/,file:src/main/resources/static/
```

### MySQL connection refused
- Check MySQL is running
- Verify port (XAMPP = 3307, default MySQL = 3306)
- Check username/password in `application.properties`

### `mvn` not recognized in terminal
Use IntelliJ's Maven panel (right side) → Lifecycle → double-click **clean** then **package**

### JWT token expired
Token lasts 30 days by default. Logout and login again.

---

## 👨‍💻 Developer

**Raj Shah** — TY B.Tech CSE Student  
Walchand Institute of Technology, Solapur


---

>Built with ❤️ using Java + Spring Boot + MySQL + Vanilla JS | Deployed on GitHub Pages + Render + Aiven
