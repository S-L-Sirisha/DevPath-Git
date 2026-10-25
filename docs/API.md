# DevPath — REST API Specification

All endpoints communicate via JSON over HTTP. Authenticated endpoints require an `Authorization: Bearer <JWT_TOKEN>` header.

---

## 1. Authentication Endpoints

### Register Student
- **Method:** `POST`
- **Path:** `/api/auth/register`
- **Access:** Public
- **Request Body:**
```json
{
  "name": "Jane Doe",
  "email": "jane@example.com",
  "password": "Password@123",
  "targetRole": "Full Stack Developer",
  "college": "Computer Science Dept"
}
```
- **Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 2,
  "name": "Jane Doe",
  "email": "jane@example.com",
  "role": "ROLE_STUDENT",
  "targetRole": "Full Stack Developer",
  "college": "Computer Science Dept"
}
```

### Login
- **Method:** `POST`
- **Path:** `/api/auth/login`
- **Access:** Public
- **Request Body:**
```json
{
  "email": "student@devpath.com",
  "password": "Student@123"
}
```
- **Response (200 OK):** Returns JWT token and authenticated user payload.

### Current User Profile
- **Method:** `GET`
- **Path:** `/api/auth/me`
- **Access:** Authenticated (`ROLE_STUDENT` or `ROLE_ADMIN`)

---

## 2. Diagnostics & Personalized Learning Endpoints

### Placement Readiness Radar
- **Method:** `GET`
- **Path:** `/api/student/radar`
- **Access:** Authenticated
- **Response (200 OK):**
```json
{
  "programmingFundamentals": 100.0,
  "dataStructuresAndAlgorithms": 100.0,
  "aptitudeAndLogical": 100.0,
  "csFundamentals": 50.0,
  "techInterviewPrep": 0.0,
  "overallReadinessIndex": 70.0,
  "strongestSkill": "Programming Fundamentals",
  "weakestSkill": "Technical Interview Preparation",
  "areasRequiringImprovement": [
    "Computer Science Fundamentals (50.0%)",
    "Technical Interview Preparation (0.0%)"
  ],
  "recommendedNextActivity": "Prioritize review of Technical Interview Preparation to elevate placement threshold.",
  "isDemoData": false,
  "totalQuestionsAttempted": 12,
  "totalCorrect": 9
}
```

### Adaptive Recommendations
- **Method:** `GET`
- **Path:** `/api/student/recommendations`
- **Access:** Authenticated
- **Description:** Rule-based diagnostic recommendations explaining *why* topics are assigned based on student weakness thresholds ($< 65\%$).

### Mistake-to-Mastery Tracker
- **Method:** `GET`
- **Path:** `/api/student/mistakes`
- **Access:** Authenticated
- **Response (200 OK):** Returns list of missed questions with diagnostic classification (`CONCEPT_MISUNDERSTANDING`, `BOUNDARY_CASE_ERROR`, `INCORRECT_LOGIC`, etc.), submitted choice, correct choice, explanation, revision topic, and follow-up tip.

### Proof of Progress Delta
- **Method:** `GET`
- **Path:** `/api/student/progress`
- **Access:** Authenticated
- **Response (200 OK):**
```json
{
  "initialScorePercentage": 66.7,
  "currentScorePercentage": 83.3,
  "percentagePointsDelta": 16.6,
  "totalAssessmentsTaken": 2,
  "totalLessonsCompleted": 1,
  "totalChallengesSolved": 2,
  "isDemoData": false
}
```

---

## 3. Assessments & Coding Arena

### Submit Assessment
- **Method:** `POST`
- **Path:** `/api/student/assessments/submit`
- **Access:** Authenticated
- **Request Body:**
```json
{
  "assessmentId": 1,
  "durationSeconds": 320,
  "answers": {
    "1": "B",
    "2": "C",
    "3": "C",
    "4": "A",
    "5": "B",
    "6": "B"
  }
}
```
- **Response (200 OK):** Complete graded report including category breakdown, mistake details, and immediate adaptive recommendations.

### Submit Code Solution
- **Method:** `POST`
- **Path:** `/api/student/challenges/submit`
- **Access:** Authenticated
- **Request Body:**
```json
{
  "challengeId": 1,
  "language": "java",
  "codeContent": "class Solution { public int[] twoSum(int[] nums, int target) { ... return new int[0]; } }"
}
```
- **Response (200 OK):**
```json
{
  "submissionId": 1,
  "status": "ACCEPTED",
  "testCasesPassed": 3,
  "totalTestCases": 3,
  "message": "All test cases passed successfully! Optimal solution detected.",
  "timeComplexity": "O(n)",
  "spaceComplexity": "O(n)"
}
```

---

## 4. DevOps Health & Fault-Injection Probes

### Live Health Check (Kubernetes Probes)
- **Method:** `GET`
- **Path:** `/api/health`
- **Access:** Public
- **Response (200 OK when Healthy):**
```json
{
  "service": "DevPath Placement Portal",
  "status": "UP",
  "version": "1.0.0",
  "buildNumber": "1",
  "environment": "local-development",
  "databaseConnected": true,
  "memory": {
    "totalMemoryMb": 108,
    "freeMemoryMb": 60,
    "maxMemoryMb": 4032
  }
}
```

### Simulate Unhealthy State (Examiner Demonstration)
- **Method:** `POST`
- **Path:** `/api/health/simulate-failure`
- **Description:** Causes `/api/health` to return `503 SERVICE UNAVAILABLE`, simulating a failed pod for Kubernetes liveness recovery testing.

### Restore Healthy State
- **Method:** `POST`
- **Path:** `/api/health/simulate-recovery`
- **Description:** Resets `/api/health` back to HTTP `200 UP`.
