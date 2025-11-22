# Gestion Surveillance Backend - System Architecture Documentation

## Table of Contents
1. [Big Picture Overview](#big-picture-overview)
2. [Core Architecture](#core-architecture)
3. [Key Components](#key-components)
4. [Data Flow & Communication](#data-flow--communication)
5. [Tech Stack & Dependencies](#tech-stack--dependencies)
6. [Execution Flow](#execution-flow)
7. [Strengths & Tradeoffs](#strengths--tradeoffs)
8. [Executive Summary](#executive-summary)

---

## Big Picture Overview

### Project Type
**REST API Backend Service** built with Spring Boot 3.4.0 and Java 17

### Purpose
This backend system manages **exam surveillance and scheduling** for an educational institution (École Nationale Supérieure d'Arts et Métiers - ENSAJ). It provides RESTful APIs to handle:
- User authentication and authorization
- Exam session scheduling and management
- Physical room/venue allocation for exams
- Teacher/invigilator assignment
- Academic structure management (departments, options, modules)
- Complex exam filtering and scheduling queries

### Problem Solved
The system addresses the challenge of coordinating large-scale exams across multiple departments, rooms, and instructors, providing a centralized backend to:
- Prevent scheduling conflicts
- Optimize room utilization
- Manage invigilator assignments
- Track exam logistics (date, time, location, participants)
- Provide secure, role-based access to exam data

---

## Core Architecture

### Architecture Pattern: **Layered Monolith**

```
┌─────────────────────────────────────────────────────────────┐
│           HTTP Clients (Web/Mobile Frontend)                │
├─────────────────────────────────────────────────────────────┤
│    Spring Security + JWT Authentication Layer               │
│    (Request → JwtAuthenticationFilter → Token Validation)   │
├─────────────────────────────────────────────────────────────┤
│         REST Controllers Layer (@RestController)            │
│    (Routes incoming HTTP requests to appropriate handlers)  │
├─────────────────────────────────────────────────────────────┤
│         Service Layer (Business Logic)                      │
│    (Implements business rules, orchestrates repositories)   │
├─────────────────────────────────────────────────────────────┤
│    Repository Layer (Spring Data JPA)                       │
│    (Data access abstraction, CRUD operations, custom queries)
├─────────────────────────────────────────────────────────────┤
│         MySQL 8.0+ Database                                 │
│    (Persistent storage, relational model)                   │
└─────────────────────────────────────────────────────────────┘
```

### Organizational Structure

```
src/main/java/ma/ensaj/GestionSurveillance/
│
├── config/
│   └── SecurityConfig.java           # Spring Security configuration, JWT setup, CORS
│
├── controllers/                       # REST API Endpoints (8 controllers)
│   ├── AuthController.java           # Authentication (signup, login) → /api/auth/**
│   ├── ExamController.java           # Exam CRUD operations → /exams
│   ├── SessionController.java        # Session management → /sessions
│   ├── DepartmentController.java     # Department CRUD → /departments
│   ├── OptionController.java         # Option (specialization) CRUD → /options
│   ├── ModuleController.java         # Module CRUD → /modules
│   ├── EnseignantController.java     # Teacher/Invigilator CRUD → /enseignants
│   ├── LocauxController.java         # Room/Venue CRUD → /locaux
│   └── UserController.java           # User management → /users
│
├── entities/                         # JPA Domain Models (8 entities)
│   ├── User.java                     # User credentials (email, hashed password)
│   ├── Session.java                  # Exam session metadata (dates, time slots)
│   ├── Exam.java                     # Core exam entity (scheduling details)
│   ├── Department.java               # Academic department
│   ├── Option.java                   # Study specialization/option
│   ├── Module.java                   # Course/subject module
│   ├── Enseignant.java              # Instructor/Invigilator
│   └── Locaux.java                   # Physical room/venue
│
├── repositories/                     # Data Access Layer (8 repositories)
│   ├── UserRepository.java           # User persistence & queries
│   ├── ExamRepository.java           # Exam persistence + custom JPQL queries
│   ├── SessionRepository.java        # Session persistence
│   ├── DepartmentRepository.java     # Department persistence
│   ├── OptionRepository.java         # Option persistence
│   ├── ModuleRepository.java         # Module persistence
│   ├── EnseignantRepository.java     # Teacher persistence
│   └── LocauxRepository.java         # Room persistence
│
├── services/                         # Business Logic Layer (8 services)
│   ├── ExamService.java              # Exam CRUD + filtering logic
│   ├── SessionService.java           # Session management
│   ├── DepartmentService.java        # Department logic
│   ├── OptionService.java            # Option logic
│   ├── ModuleService.java            # Module logic
│   ├── EnseignantService.java        # Teacher logic
│   ├── LocauxService.java            # Room logic
│   └── UserDetailsServiceImpl.java    # Spring Security user loader
│
├── security/                         # JWT & Authentication (4 classes)
│   ├── JwtService.java               # Token generation & validation
│   ├── JwtAuthenticationFilter.java  # Request interceptor for JWT
│   ├── JwtResponse.java              # DTO: Login response {token}
│   └── LoginRequest.java             # DTO: Login credentials {email, password}
│
└── GestionSurveillanceApplication.java  # Spring Boot entry point

resources/
├── application.properties            # Database, JWT, server config
├── static/                           # Static assets (CSS, JS, images)
└── templates/                        # Thymeleaf templates (if used)
```

### Design Principles Applied
- **Separation of Concerns:** Each layer handles specific responsibilities
- **Dependency Injection:** Spring manages bean lifecycle and wiring
- **Repository Pattern:** Abstracts data access logic
- **Service Layer:** Encapsulates business rules independent of HTTP
- **Stateless Authentication:** JWT tokens eliminate server-side session storage

---

## Key Components

### 1. **Authentication & Security Module** (`security/`)

**Purpose:** Manage user identity verification and protect API endpoints

**Components:**

#### `JwtService.java`
- **Responsibility:** Token lifecycle management
- **Methods:**
  - `generateToken(UserDetails)` → Creates JWT with 10-hour expiration
  - `validateToken(String token, UserDetails)` → Verifies signature and expiration
  - `extractUsername(String token)` → Parses token to retrieve email
  - `isTokenExpired(String token)` → Checks token validity
- **Algorithm:** HS256 (HMAC-SHA256) with SHA-256 hashing
- **Claims:** Username, issued time, expiration time

#### `JwtAuthenticationFilter.java`
- **Responsibility:** Intercept HTTP requests and validate JWT
- **Extends:** `OncePerRequestFilter` (runs exactly once per request)
- **Flow:**
  1. Extract `Authorization` header
  2. Parse `Bearer <token>` format
  3. Skip auth for `/api/auth/**` endpoints
  4. Validate token and load user from database
  5. Set Spring `SecurityContext` for downstream authorization
- **Security:** Prevents unauthorized access to protected resources

#### `SecurityConfig.java`
- **Responsibility:** Configure Spring Security framework
- **Key Settings:**
  - CORS enabled for `localhost:3000` and `localhost:3031`
  - CSRF disabled (stateless API)
  - Session policy: `STATELESS` (no server-side session)
  - Password encoding: BCrypt with random salt
  - Filter chain: JWT filter before `UsernamePasswordAuthenticationFilter`
- **Protected Endpoints:** All routes except `/api/auth/**` require valid JWT

---

### 2. **Controller Layer** (`controllers/`)

**Purpose:** Handle HTTP requests and route to services

**Example: `ExamController.java`**

```
Endpoint               Method   Auth  Purpose
─────────────────────────────────────────────────────────────
POST /exams            POST     ✓     Create new exam
GET /exams             GET      ✓     Retrieve all exams
GET /exams/{id}        GET      ✓     Get specific exam by ID
GET /exams/date/{date} GET      ✓     Filter exams by date
GET /exams/department/{id}
                       GET      ✓     Filter by department
GET /exams/module/{id} GET      ✓     Filter by module
GET /exams/option/{id} GET      ✓     Filter by option
GET /exams/findByDateAndTime?...
                       GET      ✓     Complex date/time filtering
PUT /exams/{id}        PUT      ✓     Update exam details
DELETE /exams/{id}     DELETE   ✓     Delete exam
```

**AuthController Pattern:**
- `POST /api/auth/signup` - Register new user (password hashed with BCrypt)
- `POST /api/auth/login` - Authenticate & return JWT token

**Pattern Applied:** RESTful conventions with HTTP status codes
- 201 Created (successful POST)
- 200 OK (successful GET/PUT)
- 204 No Content (successful DELETE)
- 400 Bad Request (invalid input)
- 401 Unauthorized (missing/invalid JWT)
- 404 Not Found (resource doesn't exist)

---

### 3. **Service Layer** (`services/`)

**Purpose:** Encapsulate business logic, coordinate repositories

**Example: `ExamService.java`**

```java
@Service
public class ExamService {
    // CRUD Operations
    ├─ saveExam(Exam exam)                      // Create
    ├─ getAllExams()                            // Read all
    ├─ getExamById(Long id)                     // Read one
    ├─ updateExam(Long id, Exam details)        // Update
    └─ deleteExam(Long id)                      // Delete

    // Filtering Operations
    ├─ getExamsByDate(LocalDate date)           // Query by date
    ├─ getExamsByDepartement(Long deptId)       // Query by department
    ├─ getExamsByModule(Long moduleId)          // Query by module
    ├─ getExamsByOption(Long optionId)          // Query by option
    └─ findByDateAndTime(date, start, end, sessionId)  // Complex query

    // Internal Flow
    ExamService → ExamRepository → MySQL Database
}
```

**Similar patterns** applied to all entity services (SessionService, DepartmentService, etc.)

---

### 4. **Repository Layer** (`repositories/`)

**Purpose:** Abstract database operations using Spring Data JPA

**Example: `ExamRepository.java`**

```java
@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {
    // Auto-generated SQL from method names
    List<Exam> findByDate(LocalDate date);
    List<Exam> findByDepartement_Id(Long departementId);
    List<Exam> findByModule_Id(Long moduleId);
    List<Exam> findByOption_Id(Long optionId);

    // Custom JPQL query for complex logic
    @Query("SELECT e FROM Exam e WHERE e.date = :date " +
           "AND e.startTime >= :startTime " +
           "AND e.endTime <= :endTime " +
           "AND e.session.id = :sessionId")
    List<Exam> findByDateAndTime(@Param("date") LocalDate date,
                                 @Param("startTime") LocalTime startTime,
                                 @Param("endTime") LocalTime endTime,
                                 @Param("sessionId") Long sessionId);
}
```

**Benefits:**
- No manual SQL writing
- Automatic parameter binding
- Type-safe queries
- Consistent error handling

---

### 5. **Entity/Domain Layer** (`entities/`)

**Purpose:** Define data structures and relationships

#### **Entity Relationship Diagram**

```
┌──────────────────────┐
│   User (Auth)        │
├──────────────────────┤
│ id (PK)              │
│ email (UNIQUE)       │
│ password (BCrypt)    │
└──────────────────────┘

                                        ┌─────────────────────┐
                                        │     Session         │
                                        ├─────────────────────┤
                                        │ session_id (PK)     │
                                        │ type                │
                                        │ startDate           │
                                        │ endDate             │
                                        │ debutMatin1/finMatin1
                                        │ debutMatin2/finMatin2
                                        │ debutSoir1/finSoir1
                                        │ debutSoir2/finSoir2
                                        └─────────────────────┘
                                                  │
                                                  │ 1:N
                                                  ↓
┌─────────────────────────────────────────┐
│            Exam (Core Entity)            │
├─────────────────────────────────────────┤
│ id (PK)                                 │
│ date                                    │
│ startTime                               │
│ endTime                                 │
│ nbrEtudiants                            │
│ session_id (FK) → Session               │
│ enseignant_id (FK) → Enseignant         │
│ departement_id (FK) → Department        │
│ option_id (FK) → Option                 │
│ module_id (FK) → Module                 │
│ locaux (M:M) → Locaux (junction table) │
└─────────────────────────────────────────┘
     │        │         │         │         │
     │        │         │         │         └──→ ┌────────────┐
     │        │         │         │              │  Module    │
     │        │         │         │              ├────────────┤
     │        │         │         │              │ id (PK)    │
     │        │         │         │              │ nom        │
     │        │         │         │              │ option_id  │
     │        │         │         │              └────────────┘
     │        │         │         │
     │        │         │         └──→ ┌────────────┐
     │        │         │              │  Option    │
     │        │         │              ├────────────┤
     │        │         │              │ id (PK)    │
     │        │         │              │ nom        │
     │        │         │              │ dept_id    │
     │        │         │              └────────────┘
     │        │         │
     │        │         └──→ ┌────────────────┐
     │        │              │ Department     │
     │        │              ├────────────────┤
     │        │              │ id (PK)        │
     │        │              │ nom            │
     │        │              └────────────────┘
     │        │
     │        └──→ ┌──────────────────┐
     │             │   Enseignant     │
     │             ├──────────────────┤
     │             │ id (PK)          │
     │             │ nom              │
     │             │ prenom           │
     │             │ department_id    │
     │             │ dispense (flag)  │
     │             └──────────────────┘
     │
     └──→ ┌──────────────────────────────────────┐
          │  exam_locaux (Junction Table - M:M)  │
          ├──────────────────────────────────────┤
          │ exam_id (FK)                         │
          │ local_id (FK)                        │
          └──────────────────────────────────────┘
                      ↓
              ┌────────────────┐
              │    Locaux      │
              ├────────────────┤
              │ id (PK)        │
              │ nom            │
              │ taille         │
              │ type           │
              └────────────────┘
```

**Key Relationships:**
- **Department 1:N Option** - Each option belongs to one department
- **Option 1:N Module** - Each module belongs to one option
- **Department 1:N Enseignant** - Teachers assigned to departments
- **Session 1:N Exam** - Multiple exams per session
- **Exam M:N Locaux** - Exams can use multiple rooms; rooms can host multiple exams

**Jackson Annotations for JSON Serialization:**
- `@JsonManagedReference` - Include in JSON output (parent side of relationship)
- `@JsonBackReference` - Exclude from JSON output (child side, prevents infinite recursion)
- `@JsonIgnoreProperties` - Ignore specific properties during serialization

---

## Data Flow & Communication

### Request Lifecycle (End-to-End)

```
1. CLIENT REQUEST
   ┌─ POST /exams
   ├─ Headers: {"Authorization": "Bearer <JWT_TOKEN>"}
   └─ Body: {exam_data_json}

2. SPRING SERVLET DISPATCHER
   └─ Routes to ExamController.createExam()

3. SECURITY FILTER CHAIN
   ┌─ JwtAuthenticationFilter intercepts
   ├─ Extracts token from Authorization header
   ├─ Parses JWT using JwtService
   ├─ Validates signature with SECRET_KEY
   ├─ Checks token expiration
   ├─ Loads user via UserDetailsServiceImpl
   ├─ Creates SecurityContext
   └─ If invalid → 401 Unauthorized response

4. CONTROLLER LAYER
   ┌─ ExamController.createExam(@RequestBody Exam exam)
   ├─ Validates request body (Spring validation)
   ├─ Calls ExamService.saveExam()
   └─ Returns ResponseEntity with status

5. SERVICE LAYER
   ┌─ ExamService.saveExam(Exam exam)
   ├─ Applies business logic/validation
   ├─ Calls ExamRepository.save()
   └─ Returns saved Exam entity

6. REPOSITORY LAYER
   ┌─ ExamRepository.save(Exam exam)
   ├─ Hibernate converts Exam to SQL INSERT
   ├─ Executes: INSERT INTO exam (date, startTime, ...)
   ├─ MySQL persists data to disk
   ├─ Auto-generates exam.id (IDENTITY strategy)
   └─ Returns persisted Exam with ID

7. RESPONSE SERIALIZATION
   ┌─ Jackson serializes Exam to JSON
   ├─ Converts LocalDate → "2025-12-01"
   ├─ Converts LocalTime → "08:00:00"
   ├─ Resolves relationships (Session, Enseignant, etc.)
   ├─ Applies @JsonIgnoreProperties to prevent cycles
   └─ Creates JSON object

8. HTTP RESPONSE
   ┌─ Status: 201 Created
   ├─ Headers: {"Content-Type": "application/json"}
   └─ Body: {exam_json_with_generated_id}

9. CLIENT RECEIVES
   ├─ Parses JSON response
   ├─ Stores exam.id for future operations
   └─ Updates UI with new exam
```

### Example: Filtering Exams by Date

```
GET /exams/date/2025-12-15
Headers: {"Authorization": "Bearer <JWT>"}

↓ [JWT Validation] ✓ Valid token, user authenticated

↓ ExamController.getExamsByDate(LocalDate.of(2025, 12, 15))

↓ ExamService.getExamsByDate(LocalDate date)

↓ ExamRepository.findByDate(date)

↓ [Hibernate generates SQL]
   SELECT e.* FROM exam e WHERE e.date = '2025-12-15'

↓ [MySQL executes and returns rows]

↓ [Hibernate maps result set to Exam objects]

↓ [Jackson serializes List<Exam> to JSON]
   {
     "status": 200,
     "body": [
       {
         "id": 1,
         "date": "2025-12-15",
         "startTime": "08:00:00",
         "endTime": "10:00:00",
         "nbrEtudiants": 45,
         "session": {
           "session_id": 1,
           "type": "Normal",
           "startDate": "2025-12-01"
         },
         "enseignant": {
           "id": 5,
           "nom": "Dupont",
           "prenom": "Jean"
         },
         "locaux": [
           {"id": 10, "nom": "Amphithéâtre A", "taille": 100},
           {"id": 11, "nom": "Salle 201", "taille": 50}
         ]
       },
       {...more exams...}
     ]
   }

↓ Browser receives 200 OK + JSON array
```

### Data Interaction Pattern

```
AuthController → UserDetailsServiceImpl → UserRepository → User table
                                            ↓
                                      Password validation (BCrypt)

ExamController → ExamService → ExamRepository ↓
                                      ├─→ exam table
                                      ├─→ Joins with session_id
                                      ├─→ Joins with enseignant_id
                                      ├─→ Joins with department_id
                                      ├─→ Joins with option_id
                                      ├─→ Joins with module_id
                                      └─→ Joins with exam_locaux (M:M junction)

DepartmentController → DepartmentService → DepartmentRepository ↓
                                      ├─→ department table
                                      ├─→ Cascades to options
                                      └─→ Cascades to enseignants
```

---

## Tech Stack & Dependencies

### Core Framework
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **Spring Boot** | 3.4.0 | Application framework | Industry standard, rapid development, built-in autoconfiguration |
| **Spring Web** | 3.4.0 | REST controller handling | Enables @RestController, @RequestMapping annotations |
| **Spring Security** | Latest | Authentication/authorization | Provides filter chain, UserDetails, principal abstractions |
| **Spring Data JPA** | Latest | ORM abstraction layer | Eliminates boilerplate SQL, auto-generates CRUD methods |

### Database & ORM
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **MySQL** | 8.0+ | Relational database | Stores all persistent data, supports complex queries |
| **Hibernate** | Latest | ORM framework | Maps Java objects to database tables, lazy loading, cascade operations |
| **JDBC Driver** | mysql-connector-j | Database connectivity | Enables Spring Data JPA to communicate with MySQL |

### Security & Authentication
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **JJWT** | 0.11.5 | JWT token handling | Standard for stateless authentication, avoids session storage |
| **BCrypt** | Built-in Spring | Password hashing | One-way encryption with salt, industry standard |

### Data Serialization
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **Jackson** | 2.15.2 | JSON serialization | Spring's default JSON processor, handles complex types |
| **jackson-datatype-jsr310** | 2.15.2 | Java 8 date/time support | Serializes LocalDate, LocalTime to ISO-8601 |
| **jackson-datatype-hibernate5** | 2.15.2 | Hibernate lazy-loading | Handles uninitialized proxies during JSON conversion |

### Utilities
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **Lombok** | Latest | Code generation | Eliminates boilerplate @Data generates getters/setters |
| **OpenCSV** | 5.7.1 | CSV processing | Potential for exam data import/export |
| **Spring DevTools** | Latest | Development aid | Live reload during development |

### Build & Dependencies
| Component | Version | Purpose | Why? |
|-----------|---------|---------|------|
| **Maven** | 3.6+ | Dependency management | Declarative POM, manages transitive dependencies |
| **Java** | 17 | Language | LTS version, strong typing, modern features (records, sealed classes) |

### Configuration Files
```properties
spring.application.name=GestionSurveillance
spring.datasource.url=jdbc:mysql://localhost:3306/surveillance_jee
spring.datasource.username=root
spring.datasource.password=                    # Empty for local dev
spring.jpa.hibernate.ddl-auto=update          # Auto-create/update tables
spring.jpa.show-sql=true                      # Log SQL queries
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
server.port=8082
jwt.secret=5f4e2f31a0918d19ad5a830fb42c8ff51b6bcd43855cd04d3cbf772b2203f1d0
logging.level.org.springframework.security=DEBUG
spring.servlet.multipart.max-file-size=10MB
```

---

## Execution Flow

### Complete User Journey: Login & Retrieve Exams

#### **Phase 1: User Registration**

```
┌─────────────────────────────────────────────────────┐
│ STEP 1: USER SIGNUP                                 │
└─────────────────────────────────────────────────────┘

Client sends:
  POST /api/auth/signup
  Content-Type: application/json
  {
    "email": "prof@ensaj.edu",
    "password": "securePass123"
  }

↓ Spring DispatcherServlet routes to AuthController

↓ AuthController.registerUser()
  ├─ Check: userRepository.existsByEmail(email)
  │  └─ Query: SELECT COUNT(*) FROM users WHERE email = 'prof@ensaj.edu'
  │  └─ Result: 0 (doesn't exist)
  ├─ Encode: passwordEncoder.encode("securePass123")
  │  └─ BCrypt generates random salt + 10 rounds
  │  └─ Result: "$2a$10$N9qo8uLO...encrypted...hash"
  ├─ Save: userRepository.save(user)
  │  └─ Hibernate: INSERT INTO users (email, password) VALUES (...)
  │  └─ MySQL: Inserts row and returns generated id
  └─ Return: ResponseEntity.ok("Utilisateur enregistré avec succès")

HTTP Response:
  Status: 200 OK
  Body: "Utilisateur enregistré avec succès"
```

#### **Phase 2: User Login & JWT Generation**

```
┌─────────────────────────────────────────────────────┐
│ STEP 2: USER LOGIN                                  │
└─────────────────────────────────────────────────────┘

Client sends:
  POST /api/auth/login
  Content-Type: application/json
  {
    "email": "prof@ensaj.edu",
    "password": "securePass123"
  }

↓ Spring DispatcherServlet routes to AuthController

↓ AuthController.loginUser()
  ├─ Create: UsernamePasswordAuthenticationToken(email, password)
  ├─ Authenticate: authenticationManager.authenticate(token)
  │  ├─ DaoAuthenticationProvider.retrieveUser(email)
  │  │  └─ UserDetailsServiceImpl.loadUserByUsername(email)
  │  │     └─ UserRepository.findByEmail(email)
  │  │        └─ Query: SELECT * FROM users WHERE email = 'prof@ensaj.edu'
  │  │        └─ Result: User entity with hashed password
  │  ├─ Compare: passwordEncoder.matches("securePass123", "$2a$10$N9qo8u...")
  │  │  └─ Compares plaintext with stored hash
  │  │  └─ Result: true (passwords match)
  │  └─ Load authorities: user.getAuthorities()
  │     └─ Result: [ROLE_USER] (default Spring role)
  ├─ Set SecurityContext: SecurityContextHolder.getContext().setAuthentication(auth)
  ├─ Extract principal: UserDetails userDetails = authentication.getPrincipal()
  ├─ Generate JWT: jwtService.generateToken(userDetails)
  │  ├─ Jwts.builder()
  │  │  .setSubject("prof@ensaj.edu")
  │  │  .setIssuedAt(now)
  │  │  .setExpiration(now + 10 hours)
  │  │  .signWith(HS256, "5f4e2f31a0918d19ad5a...secret_key")
  │  │  .compact()
  │  └─ Result: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwcm9mQGVuc2FqLmVkdSIsImlhdCI6MTczMDcwOTI0MCwiZXhwIjoxNzMwNzQ2MjQwfQ.signature_hex"
  └─ Return: ResponseEntity.ok(new JwtResponse(jwt))

HTTP Response:
  Status: 200 OK
  Content-Type: application/json
  Body:
    {
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwcm9mQGVuc2FqLmVkdSIsImlhdCI6MTczMDcwOTI0MCwiZXhwIjoxNzMwNzQ2MjQwfQ.signature_hex"
    }

Client stores token in localStorage:
  localStorage.setItem('token', 'eyJhbGciOiJIUzI1NiIs...')
```

#### **Phase 3: Authenticated Request - Get Exams**

```
┌─────────────────────────────────────────────────────┐
│ STEP 3: GET EXAMS (AUTHENTICATED)                   │
└─────────────────────────────────────────────────────┘

Client sends:
  GET /exams/date/2025-12-15
  Headers: {
    "Authorization": "Bearer eyJhbGciOiJIUzI1NiIs...",
    "Content-Type": "application/json"
  }

↓ Spring DispatcherServlet routes to ExamController

↓ SECURITY FILTER CHAIN (Before controller)
  
  ├─ JwtAuthenticationFilter.doFilterInternal() runs
  │
  ├─ Get request path: "/exams/date/2025-12-15"
  ├─ Check if path starts with "/api/auth/" → false (not auth endpoint)
  │
  ├─ Get Authorization header: "Bearer eyJhbGciOiJIUzI1NiIs..."
  ├─ Extract token: token = "eyJhbGciOiJIUzI1NiIs..."
  │
  ├─ JwtService.extractUsername(token)
  │  ├─ Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token)
  │  ├─ Extract "sub" claim: "prof@ensaj.edu"
  │  └─ Return: "prof@ensaj.edu"
  │
  ├─ Username extracted: "prof@ensaj.edu"
  ├─ Check if SecurityContext.getAuthentication() is null → yes
  │
  ├─ UserDetailsService.loadUserByUsername("prof@ensaj.edu")
  │  └─ UserRepository.findByEmail("prof@ensaj.edu")
  │     └─ Query: SELECT * FROM users WHERE email = 'prof@ensaj.edu'
  │     └─ Returns: User(id=1, email="prof@ensaj.edu", password="$2a$10$...")
  │
  ├─ JwtService.validateToken(token, userDetails)
  │  ├─ Extract username from token: "prof@ensaj.edu"
  │  ├─ Compare: userDetails.getUsername() == "prof@ensaj.edu" → true
  │  ├─ Check expiration: now < expiration → true
  │  └─ Return: true (token is valid)
  │
  ├─ Create: UsernamePasswordAuthenticationToken(userDetails, null, authorities)
  ├─ Set details: authToken.setDetails(buildDetails(request))
  │
  ├─ Set in SecurityContext:
  │  └─ SecurityContextHolder.getContext().setAuthentication(authToken)
  │
  └─ Continue filter chain: filterChain.doFilter(request, response)

✓ User is now authenticated! SecurityContext set for downstream filters

↓ ExamController.getExamsByDate(@PathVariable LocalDate date)
  ├─ Parse date: LocalDate.of(2025, 12, 15)
  ├─ Call: ExamService.getExamsByDate(date)
  │  └─ ExamRepository.findByDate(date)
  │
  ├─ [Hibernate processes method name]
  │  ├─ Method: findByDate
  │  ├─ Generate SQL: SELECT e FROM Exam e WHERE e.date = :date
  │  └─ Execute: SELECT * FROM exam WHERE date = '2025-12-15'
  │
  ├─ [MySQL executes and returns result set]
  │  │ id | date       | startTime | endTime   | nbrEtudiants | session_id | ...
  │  │ 1  | 2025-12-15 | 08:00:00  | 10:00:00  | 45           | 1          | ...
  │  │ 2  | 2025-12-15 | 14:00:00  | 16:00:00  | 30           | 1          | ...
  │
  ├─ [Hibernate maps result set to Exam entities]
  │  ├─ Lazy Loading: Session, Enseignant, Locaux NOT loaded yet
  │  └─ Returns: List<Exam> with 2 items
  │
  └─ ExamService returns List<Exam>

↓ ExamController returns: ResponseEntity.ok(exams)

↓ Jackson Serialization (before HTTP response)
  ├─ For each Exam in list:
  │  ├─ Access exam.getSession() → triggers lazy load
  │  │  └─ Hibernate: SELECT * FROM session WHERE session_id = 1
  │  │  └─ Fetches Session data
  │  │
  │  ├─ Access exam.getEnseignant() → triggers lazy load
  │  │  └─ Hibernate: SELECT * FROM enseignant WHERE id = 5
  │  │  └─ Fetches Enseignant data
  │  │
  │  ├─ Access exam.getLocaux() → triggers lazy load
  │  │  └─ Hibernate: SELECT * FROM local l
  │  │           JOIN exam_locaux el ON l.id = el.local_id
  │  │           WHERE el.exam_id = 1
  │  │  └─ Fetches Locaux data
  │  │
  │  ├─ Serialize to JSON:
  │  │  {
  │  │    "id": 1,
  │  │    "date": "2025-12-15",
  │  │    "startTime": "08:00:00",
  │  │    "endTime": "10:00:00",
  │  │    "nbrEtudiants": 45,
  │  │    "session": {
  │  │      "session_id": 1,
  │  │      "type": "Normal",
  │  │      "startDate": "2025-12-01",
  │  │      "endDate": "2025-12-31"
  │  │    },
  │  │    "enseignant": {
  │  │      "id": 5,
  │  │      "nom": "Dupont",
  │  │      "prenom": "Jean",
  │  │      "dispense": false
  │  │    },
  │  │    "locaux": [
  │  │      {
  │  │        "id": 10,
  │  │        "nom": "Amphithéâtre A",
  │  │        "taille": 100,
  │  │        "type": "Amphi"
  │  │      },
  │  │      {
  │  │        "id": 11,
  │  │        "nom": "Salle 201",
  │  │        "taille": 50,
  │  │        "type": "Salle"
  │  │      }
  │  │    ],
  │  │    "departement": {...},
  │  │    "option": {...},
  │  │    "module": {...}
  │  │  }
  │
  └─ Continue for all exams in list

↓ HTTP Response
  Status: 200 OK
  Headers: {
    "Content-Type": "application/json",
    "Cache-Control": "no-cache"
  }
  Body: [
    {exam_json_1},
    {exam_json_2}
  ]

↓ Browser receives response
  ├─ Parse JSON array
  ├─ Update UI with exam list
  └─ Display exams for 2025-12-15
```

---

## Strengths & Tradeoffs

### ✅ Strengths

#### **1. Security & Authentication**
- **Stateless JWT:** Eliminates server-side session storage; scales horizontally
- **Bcrypt Hashing:** One-way password encryption with random salt
- **Token Expiration:** 10-hour TTL reduces exposure window
- **Request-level Validation:** JwtAuthenticationFilter prevents unauthorized access upstream

#### **2. Code Organization**
- **Layered Architecture:** Clear separation of concerns (controller/service/repository)
- **Dependency Injection:** Spring manages bean lifecycle; easy to test
- **Repository Pattern:** Abstracts data access; easy to swap implementations
- **Service Layer:** Encapsulates business logic independent of HTTP

#### **3. Database & ORM**
- **Relational Model:** Enforces data integrity with foreign keys
- **Cascade Operations:** Deleting department cascades to options/teachers
- **Lazy Loading:** Related entities loaded on-demand (performance optimization)
- **Type-Safe Queries:** Spring Data JPA eliminates SQL string concatenation

#### **4. Developer Experience**
- **Rapid Development:** Spring Boot autoconfiguration, minimal XML
- **Lombok:** Reduces boilerplate 70% (no manual getters/setters)
- **DevTools:** Live reload during development
- **Comprehensive Logging:** DEBUG level logging for security troubleshooting

#### **5. Data Serialization**
- **Jackson Circular Reference Handling:** @JsonIgnoreProperties prevents infinite loops
- **Date/Time Support:** Java 8 LocalDate/LocalTime automatically serialized to ISO-8601
- **Type Safety:** Compile-time type checking reduces runtime errors

---

### ⚠️ Tradeoffs & Limitations

#### **1. N+1 Query Problem**
**Issue:** Lazy loading can cause multiple database round trips
```java
List<Exam> exams = examRepository.findAll();  // 1 query
for (Exam exam : exams) {
    Session session = exam.getSession();      // N queries (one per exam)
}
// Result: 1 + N queries instead of 1 optimized JOIN
```
**Impact:** Performance degradation with large result sets  
**Solution:** Use @Query with JOIN FETCH or eager loading strategically

#### **2. Monolithic Architecture**
**Issue:** All features bundled in single application
```
If one module (exams) has high load, entire app scales instead of just exams service
```
**Impact:** Inefficient resource utilization; hard to scale independent features  
**Solution:** Consider microservices when traffic/complexity increases

#### **3. No Caching Layer**
**Issue:** Every request hits the database
```
GET /exams (hit database)
GET /exams (hit database again - same data!)
```
**Impact:** Unnecessary database load; slower response times  
**Solution:** Introduce Redis or Spring Cache @Cacheable

#### **4. Limited Error Handling**
**Issue:** Generic exceptions not mapped to meaningful HTTP statuses
```
User not found → 500 Internal Server Error (should be 404)
Validation failure → 500 Internal Server Error (should be 400)
```
**Impact:** Confusing error messages for API clients  
**Solution:** Implement @ControllerAdvice with @ExceptionHandler

#### **5. No Pagination**
**Issue:** `findAll()` fetches entire table into memory
```java
List<Exam> exams = examRepository.findAll();  // 1,000,000 exams in memory!
```
**Impact:** OOM errors with large datasets  
**Solution:** Use Spring Data `Page<Exam> findAll(Pageable pageable)`

#### **6. Circular Reference Workarounds**
**Issue:** @JsonIgnoreProperties feels fragile
```java
@JsonIgnoreProperties("exams")  // String key can break if field renamed
private List<Exam> exams;
```
**Impact:** Refactoring risk; no compile-time safety  
**Solution:** Use Data Transfer Objects (DTOs) instead of entities

#### **7. No Audit Logging**
**Issue:** No tracking of who modified what and when
```
User deletes exam → No record of deletion
```
**Impact:** Cannot track data changes for compliance/debugging  
**Solution:** Implement Hibernate @Audited with Envers

#### **8. CORS Configuration**
**Issue:** Hardcoded localhost origins
```java
configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "http://localhost:3031"));
```
**Impact:** Production deployment requires code changes  
**Solution:** Externalize CORS config to application.properties

#### **9. No API Versioning**
**Issue:** Single API version makes backward compatibility hard
```
If you change /exams response structure, old clients break immediately
```
**Impact:** Cannot support multiple client versions  
**Solution:** Prefix routes with /api/v1/, /api/v2/

#### **10. JWT Secret in Properties**
**Issue:** Secret key visible in repository
```properties
jwt.secret=5f4e2f31a0918d19ad5a830fb42c8ff51b6bcd43855cd04d3cbf772b2203f1d0
```
**Impact:** If repo is compromised, tokens can be forged  
**Solution:** Store secrets in environment variables or key vault (AWS Secrets Manager, Vault)

---

### Performance Considerations

| Scenario | Current | Recommendation |
|----------|---------|-----------------|
| 1000 exams per query | Loads all in memory | Add pagination (Page<Exam>) |
| Repeated calls to `/exams` | Hit DB every time | Add caching (@Cacheable) |
| Exam with 10 related rooms | N+1 queries | Use JOIN FETCH in repository |
| Audit trail | Not tracked | Add Hibernate Envers |
| Complex filtering | Custom JPQL | Consider Elasticsearch for full-text search |

---

## Executive Summary

**Gestion Surveillance Backend** is a **layered REST API service** built with Spring Boot that manages exam scheduling, room allocation, and teacher assignment for an educational institution. It uses **JWT-based stateless authentication**, **Spring Data JPA** for database access, and **MySQL** for persistence, with clear separation between controllers (HTTP handling), services (business logic), and repositories (data access).

**Key strengths:** Secure JWT auth, clean layered architecture, rapid Spring Boot development, type-safe queries.  
**Key improvements needed:** Add pagination for large datasets, implement caching, handle N+1 query problems with JOIN FETCH, externalize JWT secret, add API error handling & audit logging.

---

## Quick Reference Guide

### Common API Patterns

```bash
# Authentication
POST /api/auth/signup           # Register new user
POST /api/auth/login            # Get JWT token

# CRUD Operations (all require JWT)
POST /exams                      # Create
GET /exams                       # List all
GET /exams/{id}                 # Get one
PUT /exams/{id}                 # Update
DELETE /exams/{id}              # Delete

# Filtering
GET /exams/date/{date}          # Filter by date
GET /exams/department/{id}      # Filter by department
GET /exams/module/{id}          # Filter by module
GET /exams/option/{id}          # Filter by option
GET /exams/findByDateAndTime?date=...&startTime=...&endTime=...&sessionId=...
```

### Database Connection

```properties
# MySQL 8.0+
spring.datasource.url=jdbc:mysql://localhost:3306/surveillance_jee
spring.datasource.username=root
spring.datasource.password=
```

### Deployment Port

```
Server runs on: http://localhost:8082
```

### Supported CORS Origins

```
http://localhost:3000      (Frontend A)
http://localhost:3031      (Frontend B)
```


