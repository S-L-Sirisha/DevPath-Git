# DevPath — Database Schema & Entity Relationship Model

## 1. Entity Relationship (ER) Diagram

```mermaid
erDiagram
    USERS ||--o{ STUDENT_LESSON_PROGRESS : tracks
    USERS ||--o{ ASSESSMENT_ATTEMPTS : submits
    USERS ||--o{ CODING_SUBMISSIONS : writes
    USERS ||--o{ STUDENT_MISSION_PROGRESS : undertakes

    LEARNING_PATHS ||--|{ LESSONS : contains
    LESSONS ||--o{ STUDENT_LESSON_PROGRESS : records

    ASSESSMENTS ||--|{ ASSESSMENT_QUESTIONS : links
    QUESTIONS ||--|{ ASSESSMENT_QUESTIONS : mapped_to

    ASSESSMENT_ATTEMPTS ||--|{ ASSESSMENT_ATTEMPT_ANSWERS : contains
    QUESTIONS ||--o{ ASSESSMENT_ATTEMPT_ANSWERS : evaluated_in

    CODING_CHALLENGES ||--o{ CODING_SUBMISSIONS : attempts

    PLACEMENT_MISSIONS ||--|{ MISSION_TASKS : organizes
    PLACEMENT_MISSIONS ||--o{ STUDENT_MISSION_PROGRESS : follows

    USERS {
        bigint id PK
        varchar name
        varchar email UK
        varchar password
        varchar role
        varchar target_role
        varchar college
        timestamp created_at
        timestamp updated_at
    }

    LEARNING_PATHS {
        bigint id PK
        varchar title
        varchar slug UK
        text description
        varchar category
        varchar difficulty
        int estimated_hours
        varchar icon
        int order_index
        boolean published
    }

    LESSONS {
        bigint id PK
        bigint learning_path_id FK
        varchar title
        varchar slug
        varchar category
        varchar topic
        varchar difficulty
        text summary
        longtext content
        text prerequisites
        text key_takeaways
        int read_time_minutes
        int order_index
        boolean published
    }

    QUESTIONS {
        bigint id PK
        varchar category
        varchar topic
        varchar difficulty
        text question_text
        text code_snippet
        text option_a
        text option_b
        text option_c
        text option_d
        varchar correct_option
        text explanation
        varchar mistake_category
        varchar revision_topic
        text follow_up_tip
    }

    ASSESSMENTS {
        bigint id PK
        varchar title
        text description
        varchar category
        int time_limit_minutes
        int passing_score_percentage
        int total_questions
        varchar difficulty
        boolean is_placement_mock
    }

    ASSESSMENT_ATTEMPTS {
        bigint id PK
        bigint user_id FK
        bigint assessment_id FK
        int score
        int total_marks
        double percentage
        boolean passed
        timestamp started_at
        timestamp completed_at
        int duration_seconds
    }

    ASSESSMENT_ATTEMPT_ANSWERS {
        bigint id PK
        bigint attempt_id FK
        bigint question_id FK
        varchar selected_option
        varchar correct_option
        boolean is_correct
        varchar mistake_category
        varchar topic
        varchar category
    }

    CODING_CHALLENGES {
        bigint id PK
        varchar title
        varchar slug UK
        varchar category
        varchar topic
        varchar difficulty
        longtext description
        text example_input
        text example_output
        text constraints
        text hints
        text expected_approach
        varchar time_complexity
        varchar space_complexity
        longtext starter_code_java
        longtext starter_code_js
        longtext test_cases_json
    }

    CODING_SUBMISSIONS {
        bigint id PK
        bigint user_id FK
        bigint challenge_id FK
        varchar language
        longtext code_content
        varchar status
        int test_cases_passed
        int total_test_cases
        timestamp submitted_at
    }

    PLACEMENT_MISSIONS {
        bigint id PK
        varchar title
        text description
        int duration_days
        varchar category
        varchar icon
        int order_index
    }

    MISSION_TASKS {
        bigint id PK
        bigint mission_id FK
        int day_number
        varchar task_title
        text task_description
        varchar task_type
        bigint target_reference_id
        int order_index
    }

    STUDENT_MISSION_PROGRESS {
        bigint id PK
        bigint user_id FK
        bigint mission_id FK
        varchar status
        int current_day
        int completed_tasks_count
        int total_tasks_count
        timestamp started_at
        timestamp last_activity_at
        timestamp completed_at
    }
```

---

## 2. Table Definitions & Constraints

### `users`
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique user identifier |
| `name` | VARCHAR(100) | NOT NULL | Full name of the user |
| `email` | VARCHAR(120) | NOT NULL, UNIQUE | User login email address |
| `password` | VARCHAR(100) | NOT NULL | BCrypt hash of user password |
| `role` | VARCHAR(20) | NOT NULL, DEFAULT 'ROLE_STUDENT' | Security authority (`ROLE_STUDENT`, `ROLE_ADMIN`) |
| `target_role` | VARCHAR(100) | DEFAULT 'Software Engineer' | Student placement career target |
| `college` | VARCHAR(150) | NULL | Academic institution / department |
| `created_at` | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP | Registration timestamp |
| `updated_at` | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | Last modification |

### `assessment_attempt_answers`
*Core diagnostic entity powering the Placement Readiness Radar and Mistake-to-Mastery Tracker.*
| Column | Type | Constraints | Description |
|---|---|---|---|
| `id` | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Answer identifier |
| `attempt_id` | BIGINT | NOT NULL, FK &rarr; `assessment_attempts(id)` | Parent test attempt |
| `question_id` | BIGINT | NOT NULL, FK &rarr; `questions(id)` | Attempted question |
| `selected_option` | VARCHAR(5) | NULL | Option submitted by student ('A', 'B', 'C', 'D') |
| `correct_option` | VARCHAR(5) | NOT NULL | Verified answer option |
| `is_correct` | BOOLEAN | NOT NULL, DEFAULT FALSE | Automated grading flag |
| `mistake_category` | VARCHAR(50) | NULL | Diagnostic failure classification |
| `topic` | VARCHAR(100) | NULL | Granular subject topic |
| `category` | VARCHAR(50) | NULL | Broad radar competency category |
