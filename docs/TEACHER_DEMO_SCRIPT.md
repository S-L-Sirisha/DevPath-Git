# DevPath — 10-Minute Teacher & Examiner Demonstration Script

Use this script during your DevOps Laboratory examination or viva. Follow the exact sequence below to showcase both the full-stack portal features and your complete CI/CD and container orchestration lifecycle.

---

## Part 1: Project Overview & Distinct Identity (2 Minutes)

### Talking Points:
> *"Good morning, Professor. For my DevOps Laboratory project, I have built **DevPath — A Programming Learning and Placement Preparation Portal**.*
> 
> *Conventional portals merely hand students generic question banks. DevPath is built on a different core principle:*  
> **'Do not merely tell students what to learn. Identify what they struggle with, explain why they struggle, and guide them toward measurable improvement.'**
> 
> *It features an automated **Placement Readiness Radar**, an **Adaptive Learning Engine**, a **Mistake-to-Mastery Tracker**, and an auditable **Proof of Progress** metric, backed by a production Java 21, Spring Boot, MySQL, Docker, Jenkins CI/CD, and Kubernetes stack."*

---

## Part 2: Live Application & Functional Walkthrough (3 Minutes)

1. **Launch the Portal:**
   - Open browser at **[http://localhost:8085](http://localhost:8085)**.
   - Show the Landing Page with the core tagline: *"Learn. Diagnose. Improve. Get Hired."*

2. **Login with 1-Click Demo Credentials:**
   - Click **Get Started** / **Login**.
   - Click the **Student (Alex)** shortcut button &rarr; Click **Sign In**.
   - Point to the **Student Dashboard**:
     - **Placement Readiness Radar (Chart.js):** Show the 5 axes (Programming, DSA, Aptitude, CS Fundamentals, Tech Interview). Emphasize that these scores are calculated in real time from stored question attempts in the database.
     - **Proof of Progress Banner:** Point to `+16.6%` measured improvement delta comparing baseline assessment with recent tests.
     - **Adaptive Recommendations:** Show the rule-based recommendation card. Point out the diagnostic reason: *"Recommended because your accuracy in 'Binary Trees' is only 33% (below 65% placement benchmark)."*

3. **Demonstrate Mistake-to-Mastery Tracker:**
   - Click **Mistakes** in the navbar.
   - Click the filter button for **Boundary-Case Error** and **Concept Misunderstanding**.
   - Show how each mistake details:
     - What the student selected vs. the correct answer.
     - Diagnostic explanation of *why* the answer failed.
     - Recommended revision topic.

4. **Demonstrate Admin Capabilities:**
   - Log out, then log in using the **Admin (Prof)** button (`admin@devpath.com` / `Admin@123`).
   - Navigate to **Admin** view:
     - Show total registered students, completed assessments, and lesson management.
     - Point out that backend admin endpoints (`/api/admin/**`) are secured using Spring Security role-based access control (`hasRole('ADMIN')`), not just hidden in the UI.

---

## Part 3: Automated Testing & Build Validation (2 Minutes)

1. Open PowerShell in front of the examiner:
   ```powershell
   cd C:\Users\varma\Desktop\DevPath
   ```

2. Run automated test suite:
   ```powershell
   mvn test
   ```
   **What to explain to the examiner:**
   > *"I have configured JUnit 5, Mockito, and Spring Boot MockMvc integration tests. These tests validate user authentication, the mathematical calculation of radar scores, and MockMvc testing of the `/api/health` probe."*
   - Point to `BUILD SUCCESS` with `Tests run: 10, Failures: 0, Errors: 0`.

3. Show the packaged executable fat JAR:
   ```powershell
   ls target/*.jar
   ```
   Explain how `spring-boot-maven-plugin` packages embedded Tomcat and dependencies into `BOOT-INF/`.

---

## Part 4: Docker & Multi-Service Compose (1.5 Minutes)

1. **Explain the Dockerfile:**
   - Open `Dockerfile` in the IDE.
   - Point out **Multi-Stage Build**:
     - Stage 1: `maven:3.9.6-eclipse-temurin-21-jammy` compiles and packages the JAR.
     - Stage 2: `eclipse-temurin:21-jre-jammy` contains only the minimal JRE runtime, reducing image footprint.
     - Security: Show the non-root user creation (`USER devpath`) adhering to container security best practices.
     - Health check: Explain the embedded `HEALTHCHECK` directive.

2. **Explain Docker Compose:**
   - Open `docker-compose.yml`.
   - Point to `depends_on: devpath-mysql: condition: service_healthy`.
   - Explain that the application will wait until MySQL responds to `mysqladmin ping` before starting, preventing startup race conditions.
   - Point to the persistent volume `devpath-mysql-data` ensuring student records survive container restarts.

---

## Part 5: Jenkins CI/CD Pipeline (1 Minute)

1. **Explain the Jenkinsfile:**
   - Open `Jenkinsfile` in the IDE.
   - Walk through the 8 stages:
     1. Checkout Source SCM
     2. Verify Environment (Java 21 & Maven version checks)
     3. Compile & Lint
     4. Automated Testing (JUnit test report publisher)
     5. Package Executable JAR
     6. Build Tagged Docker Image
     7. Deploy / Verify Environment
     8. Pipeline Post-Actions (notifications and cleanup)
   - Explain the cross-platform script handling using `isUnix()` to support both Windows agents (`bat`) and Linux agents (`sh`).

---

## Part 6: Kubernetes Cloud-Native & Self-Healing Demo (2 Minutes)

1. **Inspect Kubernetes Manifests:**
   - Open `k8s/` folder:
     - `01-configmap.yaml` & `02-secret.yaml`: Decoupled non-sensitive configuration and secrets.
     - `03-mysql-pvc.yaml`: 5Gi persistent volume claim.
     - `06-backend-deployment.yaml`: High-availability 2 replicas with `livenessProbe` and `readinessProbe` on `/api/health`.

2. **Live Fault-Injection & Self-Healing Demonstration:**
   - In the browser, navigate to the **DevOps Demo** page (**[http://localhost:8085/#health](http://localhost:8085/#health)**).
   - Show the live health status: `status: "UP"`, version, build number, and JVM memory.
   - Click the red button: **Simulate Unhealthy State (503)**.
   - Show the probe immediately returning `503 SERVICE UNAVAILABLE`.
   **What to tell the teacher:**
   > *"In our Kubernetes deployment, when `/api/health` returns HTTP 503 for 3 consecutive checks (`failureThreshold: 3`), the Kubernetes kubelet detects that the liveness probe has failed and automatically terminates and restarts the unhealthy pod. This demonstrates autonomous cloud-native self-healing without manual human intervention."*
   - Click **Restore Healthy State (200 UP)** to recover the probe cleanly!

---

## Part 7: Likely Viva / Exam Questions & Answers

**Q1: Why did you use Spring Security with JWT instead of standard server sessions?**  
*Answer:* Stateless JWT tokens decouple session state from the application server, allowing horizontal pod scaling in Kubernetes without needing sticky sessions or distributed session caches like Redis.

**Q2: What is the difference between a Liveness Probe and a Readiness Probe in Kubernetes?**  
*Answer:* A Readiness Probe checks whether a pod is ready to accept user network traffic (e.g., verifying that the database connection is open). A Liveness Probe checks whether the application process is alive and healthy; if it fails, Kubernetes kills and restarts the container.

**Q3: Why is non-root user execution important in Docker?**  
*Answer:* By default, containers run as UID 0 (root). If an attacker breaks out of the container via a kernel vulnerability, they would have root access to the host node. Running as a non-privileged user (`USER devpath`, UID 1001) follows the principle of least privilege.
