# Equipend — Expense Splitting & Management System

**Course:** CS452 — Database Management Systems (DBMS)  
**Architecture:** 3-Tier Web Application (React + Spring Boot + MySQL)

---

## 📌 Project Overview

**Equipend** is a full-stack web application designed to simplify group expense tracking, pairwise balance calculations, and debt settlements. Built as a DBMS course project, Equipend focuses on transactional integrity, relational schema design, on-demand balance aggregation, and stateless security.

### Key Features

- **User Authentication & Security:** User registration, login using JWT (JJWT), password hashing with BCrypt, and stateless request authorization.
- **Group Management:** Create groups with specific default currencies, invite/add members, and enforce zero-balance restrictions before removing members or deleting groups.
- **Expense Creation & Splitting:** Support for multiple split strategies (Equal, Percentage, Exact Amount), handling rounding remainders automatically for equal splits.
- **Dynamic Balance Calculation:** On-demand calculation of pairwise balances at both per-group and cross-group (total) levels without redundant/cached state.
- **Immutable Settlements:** Direct recording of payment settlements between group members with validation against current outstanding balances.
- **API Documentation:** Interactive OpenAPI/Swagger UI endpoints for API inspection and testing.

---

## 🛠 Tech Stack & Dependencies

### Frontend
- **Framework & Build:** React 19, Vite, React Router DOM v7
- **Styling & UI Icons:** Tailwind CSS v4, `@tailwindcss/vite`, Lucide React
- **State & Data Fetching:** TanStack React Query v5, Axios
- **Form Handling:** React Hook Form v8
- **Authentication:** `jwt-decode`

```json
{
  "dependencies": {
    "@tailwindcss/vite": "^4.3.3",
    "@tanstack/react-query": "^5.104.5",
    "axios": "^1.20.0",
    "jwt-decode": "^4.0.0",
    "lucide-react": "^1.61.0",
    "react": "^19.2.8",
    "react-dom": "^19.2.8",
    "react-hook-form": "^8.0.0-beta.5",
    "react-router-dom": "^7.18.4",
    "tailwindcss": "^4.3.3"
  }
}
```

### Backend
- **Framework & Runtime:** Java 21 (LTS), Spring Boot
- **Web & API:** Spring Web MVC, SpringDoc OpenAPI Starter WebMVC UI (`3.1.0`)
- **Security:** Spring Security, JJWT (`0.12.6` — `jjwt-api`, `jjwt-impl`, `jjwt-jackson`)
- **Data & Persistence:** Spring Data JPA, MySQL Connector/J
- **Utilities:** Lombok, `uuid-creator` (`6.1.1`), Spring Boot Actuator & DevTools

### Database
- **DBMS:** MySQL 8.4

---

## 📁 Repository Structure

```text
equipend/
├── frontend/                # React Single Page Application (Vite + Tailwind CSS v4)
├── backend/                 # Spring Boot REST API
└── docs/
```

For expanded view visit [Project Structure](./project-structure.txt)

---

## 🗄 Database Design & Schema Overview

The system uses a relational schema normalized to **3NF** to prevent redundant data and ensure referential integrity.

### Entities & Relationships
1. **User:** `user_id` (PK), `full_name`, `email` (Unique), `username` (Unique), `password_hash`, `date_of_birth`, `country`, `created_at`.
2. **Group:** `group_id` (PK), `name`, `description`, `currency`, `created_by` (FK → User), `created_at`.
3. **GroupMember:** Junction table `(group_id, user_id)` (Composite PK), `joined_at`.
4. **Expense:** `expense_id` (PK), `group_id` (FK → Group), `paid_by` (FK → User), `title`, `amount`, `split_type` (`EQUAL`, `PERCENTAGE`, `EXACT`), `expense_date`, `created_at`.
5. **ExpenseSplit:** Junction table `(expense_id, user_id)` (Composite PK), `share_amount`, `share_percentage`.
6. **Settlement:** `settlement_id` (PK), `group_id` (FK → Group), `paid_by` (FK → User), `paid_to` (FK → User), `amount`, `note`, `settled_at`.

> **Note:** Balances are not stored as pre-calculated columns in the database. They are dynamically aggregated on-demand from `ExpenseSplit` and `Settlement` records to guarantee strong consistency.

---

## ⚙️ Environment Variables Setup

Create a `.env` file in the root directory (or configure application properties in Spring Boot) using the template below:

```env
# Database Settings
MYSQL_ROOT_PASSWORD=your_root_password
MYSQL_DATABASE=equipend_db
MYSQL_USER=equipend_user
MYSQL_PASSWORD=your_user_password
MYSQL_HOST_PORT=3307

# JWT Security Settings
JWT_SECRET=your_super_secret_jwt_key_here_must_be_at_least_256_bits
```

---

## 🚀 Getting Started

### Prerequisites
- **Node.js:** v18+ and npm
- **Java JDK:** 21 LTS
- **Maven:** 3.8+
- **MySQL Server:** 8.0+ (or Docker installed)

---

### 1. Database Setup (Docker or Local MySQL)

#### Using Docker:
```bash
docker run -d \
  --name equipend-mysql \
  -e MYSQL_ROOT_PASSWORD=your_root_password \
  -e MYSQL_DATABASE=equipend_db \
  -e MYSQL_USER=equipend_user \
  -e MYSQL_PASSWORD=your_user_password \
  -p 3307:3306 \
  mysql:8.0
```

---

### 2. Backend Setup (Spring Boot)

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Configure `src/main/resources/application.properties` (or `application.yml`):
   ```properties
    spring.datasource.username=${MYSQL_USER}
    spring.datasource.password=${MYSQL_PASSWORD}
    spring.jpa.hibernate.ddl-auto=validate
    spring.jpa.properties.hibernate.format_sql=true
    jwt.secret=${JWT_SECRET}
    spring.jackson.datatype.datetime.write-dates-as-timestamps=false
    spring.jackson.time-zone=UTC
    spring.jackson.datatype.enum.read-enums-using-to-string=true
    spring.jackson.datatype.enum.write-enums-using-to-string=true

   jwt.secret=your_super_secret_jwt_key_here_must_be_at_least_256_bits
   ```
3. Build and run the backend application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The API will be available at `http://localhost:8080`.

---

### 3. Frontend Setup (React + Vite)

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Run the development server:
   ```bash
   npm run dev
   ```
   The application will be accessible at `http://localhost:5173`.

---

## 🔌 API Endpoints Reference

### Primary Endpoint Overview:
- **Authentication:**
  - `POST /api/v1/auth/register` — Register a new user
  - `POST /api/v1/auth/authenticate` — Authenticate user & receive JWT token
- **User:**
  - `GET /api/v1/user/search?query={query}` — Search users by search term
  - `GET /api/v1/user/{id}` — Get user details by UUID
- **Groups:**
  - `POST /api/v1/group` — Create a new expense group
  - `GET /api/v1/groups/{id}` — Fetch group by id
- **Expenses:**
  - `POST /api/v1/expense` — Create a new Expense
  - `GET /api/v1/expense/{id}` — Fetch expense by id
  - `GET /api/v1/expense/creator/{id}` — Fetch expense by creator id
  - `GET /api/v1/expense/payer/{id}` — Fetch expense by payer id
  - `GET /api/v1/expense/group/{id}` — Fetch expenses by group id
  - `GET /api/v1/expense/count-by-group/{id}` — Fetch Count of group expenses
- **Splits:**
  - `GET /api/v1/splits/expense/{expenseId}/user/{userId}` — Get specific split details
  - `GET /api/v1/splits/expense/{id}` — Get all splits for an expense
  - `GET /api/v1/splits/user/{id}` — Get all splits for a user
- **Group Members:**
  - `POST /api/v1/member` — Add a new group member
  - `GET /api/v1/member/group/{groupId}/user/{userId}` — Get specific group member relationship
  - `GET /api/v1/member/group/{id}` — List all members of a group
  - `GET /api/v1/member/user/{id}` — List all group memberships for a user
- **Settlements:**
  - `POST /api/v1/settlement` — Record a payment settlement
  - `GET /api/v1/settlement/{id}` — Get settlement details by UUID
  - `GET /api/v1/settlement/group/{id}` — Get all settlements for a group
  - `GET /api/v1/settlement/count-by-group/{id}` — Count total settlements in a group
  - `GET /api/v1/settlement/payer/{id}` — Get settlements made by a payer
  - `GET /api/v1/settlement/payee/{id}` — Get settlements received by a payee
  - `GET /api/v1/settlement/group/{groupId}/payer/{userId}` — Get settlements made by a payer in a specific group
  - `GET /api/v1/settlement/group/{groupId}/payee/{userId}` — Get settlements received by a payee in a specific group

---

## 👥 Course & Acknowledgments

- **Course:** CS452 — Database Management Systems
- **Project:** Equipend Course Project

## 📄 License

This project is licensed under the APACHE 2.0 License - see the [LICENSE](LICENSE) file for details.