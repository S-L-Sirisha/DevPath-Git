# DevPath — Windows 11 Local Setup & Troubleshooting Guide

This guide is tailored specifically for students running Windows 11 with PowerShell.

---

## 1. Prerequisites Check

Open PowerShell and verify your installed tools:
```powershell
java -version    # Must be Java 21 LTS
mvn -version     # Must be Apache Maven 3.9+
git --version    # Must be Git 2.40+
docker --version # Docker Desktop
```

> **Note on Port 8080 vs 8085:**  
> On many Windows student machines, the Oracle Database Listener (`TNSLSNR.exe`) or Windows services occupy port 8080. DevPath is pre-configured to run on **port 8085** to ensure zero port collision out of the box!

---

## 2. Option A: Instant Zero-Setup Run (Recommended for First Run & Grading)

This option uses the embedded in-memory database and embedded Apache Tomcat. No MySQL installation or Docker configuration is required.

1. Open PowerShell in the project directory:
   ```powershell
   cd C:\Users\varma\Desktop\DevPath
   ```

2. Compile and run automated tests:
   ```powershell
   mvn test
   ```
   *Expected outcome:* `BUILD SUCCESS` with 10 passing tests.

3. Start the application:
   ```powershell
   mvn spring-boot:run
   ```
   *Alternatively, run the pre-built executable fat JAR:*
   ```powershell
   java -jar target/devpath-portal-1.0.0.jar
   ```

4. Open your browser:
   - Web Application: **[http://localhost:8085](http://localhost:8085)**
   - Health Probe: **[http://localhost:8085/api/health](http://localhost:8085/api/health)**
   - H2 Database Console: **[http://localhost:8085/h2-console](http://localhost:8085/h2-console)** (JDBC URL: `jdbc:h2:mem:devpath`, Username: `sa`, Password: empty)

---

## 3. Option B: Running with Docker & Docker Compose (Multi-Service Production Stack)

This option builds the production multi-stage Docker image and spins up both DevPath and a dedicated MySQL 8.0 container.

1. Launch **Docker Desktop** from your Windows Start Menu and ensure the engine has started.
2. Build and launch the containers:
   ```powershell
   docker compose up --build
   ```
3. Docker Compose will:
   - Start `devpath-mysql` container and initialize the volume `devpath-mysql-data`.
   - Poll MySQL health until `mysqladmin ping` returns healthy.
   - Start `devpath-app` and execute Flyway schema migrations against MySQL.
4. Access the application on **[http://localhost:8085](http://localhost:8085)**.
5. To stop the containers while preserving database records:
   ```powershell
   docker compose down
   ```

---

## 4. Option C: Running on Kubernetes (Docker Desktop Kubernetes)

1. Open Docker Desktop Settings &rarr; **Kubernetes** &rarr; Check **Enable Kubernetes** &rarr; Click **Apply & Restart**.
2. Verify `kubectl` context:
   ```powershell
   kubectl get nodes
   ```
3. Build the local container image:
   ```powershell
   docker build -t devpath-portal:latest .
   ```
4. Deploy the Kubernetes manifests in sequence:
   ```powershell
   kubectl apply -f k8s/01-configmap.yaml
   kubectl apply -f k8s/02-secret.yaml
   kubectl apply -f k8s/03-mysql-pvc.yaml
   kubectl apply -f k8s/04-mysql-deployment.yaml
   kubectl apply -f k8s/05-mysql-service.yaml
   kubectl apply -f k8s/06-backend-deployment.yaml
   kubectl apply -f k8s/07-backend-service.yaml
   ```
   *(Or apply all at once: `kubectl apply -f k8s/`)*
5. Inspect running pods and services:
   ```powershell
   kubectl get pods -l app=devpath-portal
   kubectl get services -l app=devpath-portal
   ```
6. Access the Kubernetes application:
   - Using NodePort: **[http://localhost:30085](http://localhost:30085)**
   - Or port-forwarding to port 8085:
     ```powershell
     kubectl port-forward svc/devpath-backend-service 8085:8085
     ```

---

## 5. Troubleshooting Common Windows Errors

### Issue: "Port 8085 is already in use"
Check which process is listening on port 8085:
```powershell
Get-NetTCPConnection -LocalPort 8085
```
Stop the process using its PID:
```powershell
Stop-Process -Id <PID> -Force
```

### Issue: "failed to connect to the docker API at npipe:////./pipe/dockerDesktopLinuxEngine"
This means Docker Desktop is not launched. Start **Docker Desktop** from the Windows taskbar/Start menu and wait until the status indicator turns green.

### Issue: "Execution of scripts is disabled on this system" (PowerShell Policy)
Run:
```powershell
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser
```
