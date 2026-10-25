# DevPath — Programming Learning & Placement Preparation Portal

[![Java](https://img.shields.io/badge/Java-21%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Multi--Stage-blue.svg)](https://www.docker.com/)
[![Kubernetes](https://img.shields.io/badge/Kubernetes-Cloud--Native-326ce5.svg)](https://kubernetes.io/)
[![Jenkins](https://img.shields.io/badge/Jenkins-CI%2FCD%20Pipeline-red.svg)](https://www.jenkins.io/)
[![Tests](https://img.shields.io/badge/Tests-JUnit%205%20Passing-success.svg)]()

> **"Learn. Diagnose. Improve. Get Hired."**  
> *Do not merely tell students what to learn. Identify what they struggle with, explain why they struggle, and guide them toward measurable improvement.*

---

## 1. Project Overview

**DevPath** is a complete, full-stack programming learning and technical placement preparation portal built as a university DevOps Laboratory submission. It provides an authentic end-to-end software engineering and continuous delivery lifecycle spanning **Git**, **Maven**, **Java 21**, **Spring Boot**, **JUnit 5**, **Jenkins CI/CD**, **Docker containerization**, and **Kubernetes orchestration**.

Unlike generic online course platforms, DevPath focuses on diagnostic skill-gap analysis, root-cause mistake categorization, and transparent adaptive recommendations.

---

## 2. Core Differentiating Features

### A. Placement Readiness Radar
- Measures student competency across 5 essential placement dimensions:
  1. **Programming Fundamentals**
  2. **Data Structures and Algorithms (DSA)**
  3. **Aptitude and Logical Reasoning**
  4. **Computer Science Fundamentals (OS, DBMS, Networks)**
  5. **Technical Interview Preparation**
- Uses **Chart.js** client-side canvas rendering dynamically bound to live assessment records.
- Identifies the student's strongest skill, weakest focus area, and recommended next curricular step.

### B. Adaptive Learning Path
- Rule-based diagnostic recommendation engine.
- Pinpoints topics where a student's accuracy falls below the $65\%$ placement readiness benchmark.
- Recommends targeted concept lessons, worked examples, and coding challenges with explicit explanations of *why* the recommendation was made.

### C. Mistake-to-Mastery Tracker
- Classifies failed quiz questions into discrete cognitive error buckets:
  - `CONCEPT_MISUNDERSTANDING`: Foundational gaps in principles or mechanics.
  - `INCORRECT_LOGIC`: Faulty algorithmic deductions.
  - `BOUNDARY_CASE_ERROR`: Failure to guard edge cases (null inputs, zero, overflow).
  - `SYNTAX_OR_TYPE_TRAP`: Language nuances (pass-by-value vs reference, HTTP idempotency).
- Details submitted option, correct option, in-depth explanation, and recommended revision topics.

### D. Placement Mission Mode
- Structured multi-day sprint roadmaps (e.g., **7-Day DSA Sprint**, **Java & Backend Interview Mastery**, **Aptitude Marathon**).
- Day-by-day task checklist with completion checkboxes, milestones, and resume capability.

### E. Proof of Progress
- Auditable comparison between a student's initial baseline assessment and recent performances.
- Calculates exact percentage point deltas (e.g., $40.0\% \to 75.0\% = +35.0\%$ improvement).

### F. Coding Practice Arena
- Problem statements with difficulty badges, input/output specifications, constraints, hints, optimal approaches, and time/space complexity analysis.
- Safe static pattern evaluator ensuring no untrusted code execution on the application server.

### G. Placement Preparation Zone
- Timed diagnostic assessments with live countdown timer, question palette navigation, and instant grading.

### H. Learning Hub
- Curricular lessons categorized by language, topic, and difficulty with syntax-highlighted code blocks, prerequisites, and key takeaways.

### I. Student & Administrator Role-Based Access Control
- Secured using **Spring Security** and **JWT** (stateless tokens).
- Role-protected endpoints (`ROLE_STUDENT` vs `ROLE_ADMIN`).
- Dedicated Admin dashboard with platform statistics, lesson publishing, and student attempt monitoring.

---

## 3. Technology Stack

| Layer | Technology |
|---|---|
| **Frontend** | HTML5, CSS3 (Custom Dark/Navy Design System), Vanilla JavaScript (ES6), Chart.js |
| **Backend** | Java 21 LTS, Spring Boot 3.3.4, Spring Web, Spring Data JPA, Spring Security, Bean Validation, Actuator |
| **Authentication** | JSON Web Tokens (`jjwt-api:0.12.6`), BCrypt Password Encoding |
| **Database** | MySQL 8.0 (Production) / H2 In-Memory (Zero-Setup Dev & Test Mode), Flyway Migrations |
| **Testing** | JUnit 5, Mockito, Spring Boot MockMvc Integration Tests |
| **Build & CI/CD** | Apache Maven 3.9+, Jenkins 8-Stage Declarative Pipeline |
| **Containers** | Docker (Multi-stage build, non-root security), Docker Compose |
| **Orchestration** | Kubernetes 1.30+ (Deployments, Services, ConfigMaps, Secrets, PVC, Liveness/Readiness Probes) |

---

## 4. Pre-Seeded Demonstration Credentials

The application automatically seeds the database with initial accounts, rich lessons, questions, and baseline test attempts on startup:

| Role | Email | Password | Access Capabilities |
|---|---|---|---|
| **Demo Student** | `student@devpath.com` | `Student@123` | Dashboard, Radar, Mistakes Tracker, Learning Hub, Coding Arena, Quizzes |
| **Administrator** | `admin@devpath.com` | `Admin@123` | Admin Portal, Lesson/Question CRUD, Platform Analytics, Student Attempts |

*(1-click shortcut buttons are provided on the Login modal for instant access during presentations).*

---

## 5. Quick Start on Windows 11

### Option A: Instant Zero-Setup Run (Recommended)
No Docker or external database required! Uses the embedded in-memory database on port **8085**:

```powershell
# 1. Clone repository and navigate to folder
cd C:\Users\varma\Desktop\DevPath

# 2. Run automated tests (10 tests should pass)
mvn test

# 3. Start application
mvn spring-boot:run
```

*Alternatively, run the pre-built executable fat JAR:*
```powershell
java -jar target/devpath-portal-1.0.0.jar
```

Open browser at **[http://localhost:8085](http://localhost:8085)**.

---

### Option B: Multi-Service Docker Compose (with MySQL 8.0)

```powershell
# Build and run backend container + MySQL container
docker compose up --build
```
- Web Application: **[http://localhost:8085](http://localhost:8085)**
- Persistent volume `devpath-mysql-data` ensures data is saved across restarts.
- To shut down: `docker compose down`

---

### Option C: Kubernetes Deployment (Docker Desktop K8s)

```powershell
# 1. Build local container image
docker build -t devpath-portal:latest .

# 2. Apply all Kubernetes manifests
kubectl apply -f k8s/

# 3. Check running pods and services
kubectl get pods -l app=devpath-portal
kubectl get services -l app=devpath-portal
```
Access via NodePort at **[http://localhost:30085](http://localhost:30085)** or port-forward `kubectl port-forward svc/devpath-backend-service 8085:8085`.

---

## 6. Project Documentation Index

- **[System Architecture Documentation](docs/ARCHITECTURE.md):** Architectural diagrams, service interactions, diagnostic algorithms, and security model.
- **[Database Schema & ER Model](docs/SCHEMA.md):** Complete relational schema, Mermaid ER diagram, table constraints, and foreign keys.
- **[REST API Documentation](docs/API.md):** Full endpoint list, request/response JSON payloads, and HTTP status codes.
- **[DevOps Syllabus Mapping](docs/DEVOPS_SYLLABUS.md):** Detailed academic mapping of Git, Maven, JUnit, Jenkins, Docker, and Kubernetes concepts to project files.
- **[Windows 11 Setup Guide](docs/LOCAL_SETUP_GUIDE.md):** Beginner-friendly guide with troubleshooting for port conflicts and Docker WSL2 engine.
- **[Teacher Demonstration Script](docs/TEACHER_DEMO_SCRIPT.md):** Step-by-step 10-minute presentation guide with live fault-injection and self-healing demo.

---

## 7. Directory Structure

```
DevPath/
├── .dockerignore
├── .env.example
├── .gitignore
├── Dockerfile
├── docker-compose.yml
├── Jenkinsfile
├── pom.xml
├── README.md
├── docs/
│   ├── ARCHITECTURE.md
│   ├── SCHEMA.md
│   ├── API.md
│   ├── DEVOPS_SYLLABUS.md
│   ├── LOCAL_SETUP_GUIDE.md
│   └── TEACHER_DEMO_SCRIPT.md
├── k8s/
│   ├── 01-configmap.yaml
│   ├── 02-secret.yaml
│   ├── 03-mysql-pvc.yaml
│   ├── 04-mysql-deployment.yaml
│   ├── 05-mysql-service.yaml
│   ├── 06-backend-deployment.yaml
│   └── 07-backend-service.yaml
└── src/
    ├── main/
    │   ├── java/com/devpath/
    │   │   ├── DevPathApplication.java
    │   │   ├── config/ (DataInitializer, SecurityConfig)
    │   │   ├── controller/ (Auth, Diagnostic, Learning, Assessment, Coding, Mission, Admin, Health)
    │   │   ├── dto/ (Auth, Radar, Recommendations, Mistakes, Progress, Submissions)
    │   │   ├── entity/ (User, LearningPath, Lesson, Question, Assessment, Challenge, Mission)
    │   │   ├── exception/ (GlobalExceptionHandler, BadRequest, ResourceNotFound)
    │   │   ├── repository/ (13 JPA Repositories)
    │   │   ├── security/ (JwtUtils, AuthTokenFilter, UserDetailsServiceImpl)
    │   │   └── service/ (Auth, Diagnostic, Learning, Assessment, Coding, Mission, Admin)
    │   └── resources/
    │       ├── application.yml
    │       ├── application-dev.yml
    │       ├── application-prod.yml
    │       ├── db/migration/
    │       │   └── V1__init_schema.sql
    │       └── static/
    │           ├── index.html
    │           ├── css/ (style.css, components.css)
    │           └── js/ (api.js, state.js, app.js)
    └── test/
        ├── java/com/devpath/ (AuthServiceTest, DiagnosticServiceTest, HealthControllerIntegrationTest)
        └── resources/
            └── application-test.yml
```

---

## 8. License & Academic Attribution
Developed for University DevOps Laboratory Submission.  
Designed under the DevOps principles of automation, continuous testing, infrastructure-as-code, and resilient cloud-native architectures.
