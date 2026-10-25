# DevPath — DevOps Syllabus Academic Mapping

This document details how each core subject area in the university DevOps Laboratory syllabus is implemented, demonstrated, and validated within the **DevPath** repository.

---

## 1. Syllabus Matrix Overview

| Syllabus Topic | Implemented Artifact / Feature | Verification Command | Viva / Lab Concept |
|---|---|---|---|
| **Version Control (Git)** | `.gitignore`, branch workflows, commit history | `git status`, `git log --oneline` | Feature branching, exclusion rules, commit hygiene |
| **Build Automation (Maven)** | `pom.xml`, dependency management, plugins | `mvn clean compile test package` | Maven lifecycle, transitive dependencies, fat JARs |
| **Automated Testing** | JUnit 5 unit tests, Mockito mocks, MockMvc tests | `mvn test` | Unit vs Integration testing, Surefire reports, CI gates |
| **CI/CD Pipelines (Jenkins)** | Declarative `Jenkinsfile` (8 distinct stages) | Jenkins Web Console / Pipeline Execution | Continuous Integration, build triggers, artifact promotion |
| **Containerization (Docker)** | Multi-stage `Dockerfile`, non-root user, `.dockerignore` | `docker build -t devpath-portal .` | Layer caching, attack-surface minimization, distroless runtimes |
| **Multi-Service Orchestration** | `docker-compose.yml` (App + MySQL + Volume) | `docker compose up -d` | Service dependency ordering, healthcheck checks, persistence |
| **Container Orchestration (K8s)** | 7 Manifests (`k8s/*.yaml`): Deployments, Services, ConfigMaps | `kubectl apply -f k8s/` | Self-healing, replicas, NodePort vs ClusterIP, Pod lifecycles |
| **Reliability & Monitoring** | `/api/health`, Spring Boot Actuator, JVM memory | `curl http://localhost:8085/api/health` | Liveness/Readiness probes, graceful recovery, fault-injection |

---

## 2. Deep Dive by Topic

### A. Git & Source Code Management
- **Repository Initialization:** Managed via `.git` with clean initial tracking.
- **`.gitignore` Best Practice:** Strictly isolates JVM runtime logs, OS artifacts (`.DS_Store`, `Thumbs.db`), IDE configuration (`.idea`, `.vscode`), environment files (`.env`), and Maven build artifacts (`target/`).
- **Recommended Branching Workflow for Lab:**
  - `main`: Production-ready, tagged deployment releases.
  - `develop`: Staging integration branch.
  - `feature/*`: Granular feature branches (e.g., `feature/radar-algorithm`).

### B. Maven Lifecycle & Dependency Management
- **Parent Inheritance:** Inherits from `spring-boot-starter-parent:3.3.4` for curated dependency versions.
- **Key Plugins Configured in `pom.xml`:**
  - `maven-compiler-plugin`: Configured for Java 21 compilation with `-parameters` flag.
  - `maven-surefire-plugin`: Executes JUnit 5 unit and integration suites, emitting XML reports to `target/surefire-reports/`.
  - `spring-boot-maven-plugin`: Repackages the standard archive into an executable fat JAR with embedded Apache Tomcat and all dependencies bundled in `BOOT-INF/lib/`.

### C. Continuous Integration with Jenkins
The `Jenkinsfile` contains an 8-stage declarative pipeline:
1. **Checkout Source:** Checks out branch from Git repository.
2. **Verify Environment:** Confirms Java 21 and Maven toolchain availability.
3. **Compile & Build:** Runs `mvn clean compile -B`.
4. **Automated Testing:** Runs `mvn test -B`, parses results, and publishes test trends via the `junit` step.
5. **Package Application:** Executes `mvn package -DskipTests -B` and archives `target/*.jar`.
6. **Build Docker Image:** Builds tagged Docker container `devpath-portal:${BUILD_NUMBER}`. If Docker daemon is unreachable, logs clear diagnostic instructions without fabricating results.
7. **Deploy / Verify:** Validates target cluster or Docker Compose environment.
8. **Post-Build Actions:** Notifies on build outcome and cleans workspace.

### D. Docker Containerization
- **Multi-Stage Build Pattern:**
  - *Stage 1 (`builder`):* Uses `maven:3.9.6-eclipse-temurin-21-jammy` to download dependencies and compile.
  - *Stage 2 (`runner`):* Copies only the single built `app.jar` into a clean `eclipse-temurin:21-jre-jammy` image. This reduces image size by over 60%!
- **Non-Root Execution:** Creates system user `devpath` (UID 1001) and drops root privileges (`USER devpath`) to comply with enterprise container security guidelines (CIS Benchmark).
- **Embedded Health Check:** Uses `HEALTHCHECK` with curl hitting `http://localhost:8085/api/health`.

### E. Docker Compose (Multi-Service Stack)
- Declares two communicating services: `devpath-app` and `devpath-mysql`.
- **Database Readiness Gate:** `devpath-app` defines `depends_on: devpath-mysql: condition: service_healthy`. The Spring Boot backend will *not* start until MySQL responds successfully to `mysqladmin ping`.
- **Data Persistence:** Uses named Docker volume `devpath-mysql-data` so student assessment attempts and test records persist across container restarts.

### F. Kubernetes (K8s) Cloud-Native Deployment
Seven production-ready YAML manifests in `k8s/`:
- `01-configmap.yaml`: Stores non-sensitive parameters (`PORT: "8085"`, `SPRING_PROFILES_ACTIVE: "prod"`, `DB_NAME: "devpath_db"`).
- `02-secret.yaml`: Secure credential decoupling for database credentials and JWT signing secret.
- `03-mysql-pvc.yaml`: 5Gi storage volume claim.
- `04-mysql-deployment.yaml`: MySQL 8.0 Deployment with volume mounts and resource requests/limits.
- `05-mysql-service.yaml`: Internal ClusterIP service exposing MySQL on port 3306.
- `06-backend-deployment.yaml`: High-availability 2-replica deployment with:
  - `imagePullPolicy: IfNotPresent` (optimized for local Docker Desktop Kubernetes).
  - `livenessProbe`: Verifies process health on `/api/health`. Triggers pod restart on unhandled deadlock or crash.
  - `readinessProbe`: Verifies database connection before routing ingress traffic to pod.
  - `resources`: CPU requests (250m) and limits (1000m); Memory requests (512Mi) and limits (1024Mi).
- `07-backend-service.yaml`: NodePort service exposing DevPath on port 8085 / 30085.

### G. Reliability, Observability & Self-Healing
- `/api/health` returns real-time JVM heap metrics, system uptime, and database ping status.
- Interactive fault injection: `/api/health/simulate-failure` forces a 503 HTTP status, allowing students to demonstrate live Kubernetes self-healing and pod resurrection to their examiner.
