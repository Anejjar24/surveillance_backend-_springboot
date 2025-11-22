# Gestion Surveillance Backend - Quick Reference

## 🎯 One-Line Summary

**Layered Spring Boot REST API** that manages exam scheduling, room allocation, and teacher assignment with JWT authentication and MySQL persistence.

---

## 📊 System Architecture at a Glance

```
HTTP Request
    ↓
Spring Security (JWT Filter)
    ↓
REST Controller (Route to handler)
    ↓
Service Layer (Business logic)
    ↓
Repository Layer (Data access)
    ↓
MySQL Database
    ↓
Jackson Serialization → JSON Response
```

---

## 🔑 Key Components


| Layer        | Component                                           | Responsibility                                      |
| ------------ | --------------------------------------------------- | --------------------------------------------------- |
| **Security** | JwtService, JwtAuthenticationFilter, SecurityConfig | Token generation, validation, Spring Security setup |
| **HTTP**     | 8 Controllers (Exam, Session, Department, etc.)     | Route requests to services                          |
| **Business** | 8 Services (ExamService, SessionService, etc.)      | Implement CRUD + filtering logic                    |
| **Data**     | 8 Repositories                                      | Abstract database queries using Spring Data JPA     |
| **Models**   | 8 Entities                                          | Java objects mapped to database tables              |
| **Database** | MySQL 8.0+                                          | Persistent storage                                  |

---

## 🔐 Authentication Flow

```
1. POST /api/auth/login {email, password}
   ↓
2. AuthenticationManager validates credentials
   ↓
3. JwtService generates JWT token (10-hour expiration)
   ↓
4. Client stores token → uses in "Authorization: Bearer <token>" header
   ↓
5. JwtAuthenticationFilter validates token on every request
   ↓
6. If valid → request proceeds; if invalid → 401 Unauthorized
```

---

## 🗄️ Data Model

**Core Entity: Exam**

- Connects Session (when) + Enseignant (who) + Locaux (where) + Module (what)
- Many-to-Many relationship with Locaux (multiple rooms per exam)

**Hierarchy:**

```
Department
  ├─ Options
  │   ├─ Modules
  │   └─ Enseignants
  └─ Enseignants
      └─ Exam assignments

Session
  ├─ Exams
  └─ Time slots (morning/evening)
```

---

## 📡 API Endpoints

### Authentication

```
POST /api/auth/signup      Create user account
POST /api/auth/login       Get JWT token
```

### CRUD (all require JWT)

```
POST /exams                Create exam
GET /exams                 List all
GET /exams/{id}            Get one
PUT /exams/{id}            Update
DELETE /exams/{id}         Delete

[Similar patterns for /sessions, /departments, /options, /modules, /enseignants, /locaux]
```

### Filtering

```
GET /exams/date/{date}                                     By date
GET /exams/department/{id}                                 By department
GET /exams/module/{id}                                     By module
GET /exams/findByDateAndTime?date=...&startTime=...&...    Complex query
```

---

## 💪 Strengths

✅ **Security:** Stateless JWT, BCrypt passwords, request-level validation
✅ **Architecture:** Clean layered design, easy to test, maintainable
✅ **Developer Experience:** Spring Boot auto-config, Lombok, rapid development
✅ **Database:** Relational model with cascade operations, type-safe queries
✅ **JSON Handling:** Jackson with circular reference prevention

---

## 🚀 Typical Request Flow (Example)

```
Client: GET /exams/date/2025-12-15 + Bearer JWT

1. Spring routes to ExamController
2. JwtAuthenticationFilter validates JWT
3. ExamController.getExamsByDate() parses date parameter
4. ExamService calls ExamRepository.findByDate()
5. Hibernate generates SQL: SELECT * FROM exam WHERE date = '2025-12-15'
6. MySQL returns rows
7. Hibernate maps to Exam objects (lazy loads Session, Enseignant, Locaux)
8. Jackson serializes to JSON (applies @JsonIgnoreProperties for circular refs)
9. Spring returns 200 OK + JSON array

Result: Client receives exam list for that date with all related data
```

---

## 🛠️ Tech Stack


| Component       | Version | Purpose               |
| --------------- | ------- | --------------------- |
| Spring Boot     | 3.4.0   | Framework             |
| Spring Security | Latest  | Authentication        |
| Spring Data JPA | Latest  | ORM                   |
| Hibernate       | Latest  | Persistence           |
| MySQL           | 8.0+    | Database              |
| JWT (JJWT)      | 0.11.5  | Tokens                |
| Jackson         | 2.15.2  | JSON                  |
| Lombok          | Latest  | Boilerplate reduction |
| Java            | 17      | Language              |

# 🔒 Security Checklist

- [X]  Password hashing (BCrypt)
- [X]  JWT token expiration (10 hours)
- [X]  Request-level authentication
- [X]  CORS configuration

---

## 📝 Configuration File Location

```
src/main/resources/application.properties

Key settings:
- spring.datasource.url = MySQL connection string
- spring.datasource.username = DB user
- jwt.secret = Token signing key
- server.port = 8082
```

---

## 🎓 Quick Learning Path

1. **Start with:** `GestionSurveillanceApplication.java` (entry point)
2. **Understand:** `SecurityConfig.java` (how auth works)
3. **Explore:** `AuthController.java` (login/signup flow)
4. **Review:** `ExamController.java` + `ExamService.java` (CRUD pattern)
5. **Study:** `ExamRepository.java` (database queries)
6. **Check:** `Exam.java` entity (relationships)

---

## 🔗 Related Files

- **Main docs:** `ARCHITECTURE.md` (comprehensive)
- **Configuration:** `pom.xml` (dependencies)
- **Database schema:** Auto-generated by Hibernate from entities

---
