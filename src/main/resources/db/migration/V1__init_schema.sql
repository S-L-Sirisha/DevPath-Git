-- ========================================================
-- DevPath Database Migration: V1__init_schema.sql
-- ========================================================

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_STUDENT',
    target_role VARCHAR(100) DEFAULT 'Software Engineer',
    college VARCHAR(150) DEFAULT 'Computer Science Department',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS learning_paths (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL UNIQUE,
    description TEXT,
    category VARCHAR(50) NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    estimated_hours INT DEFAULT 10,
    icon VARCHAR(50) DEFAULT 'book-open',
    order_index INT DEFAULT 0,
    published BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS lessons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    learning_path_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL,
    category VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'BEGINNER',
    summary TEXT,
    content LONGTEXT,
    prerequisites TEXT,
    key_takeaways TEXT,
    read_time_minutes INT DEFAULT 10,
    order_index INT DEFAULT 0,
    published BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (learning_path_id) REFERENCES learning_paths(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_lesson_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    lesson_id BIGINT NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE,
    completed_at TIMESTAMP NULL,
    UNIQUE KEY uk_user_lesson (user_id, lesson_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'EASY',
    question_text TEXT NOT NULL,
    code_snippet TEXT,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_option VARCHAR(5) NOT NULL,
    explanation TEXT,
    mistake_category VARCHAR(50) NOT NULL,
    revision_topic VARCHAR(150),
    follow_up_tip TEXT
);

CREATE TABLE IF NOT EXISTS assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    category VARCHAR(50) NULL,
    time_limit_minutes INT NOT NULL DEFAULT 15,
    passing_score_percentage INT DEFAULT 60,
    total_questions INT DEFAULT 10,
    difficulty VARCHAR(20) DEFAULT 'MEDIUM',
    is_placement_mock BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS assessment_questions (
    assessment_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    PRIMARY KEY (assessment_id, question_id),
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assessment_attempts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    assessment_id BIGINT NOT NULL,
    score INT NOT NULL DEFAULT 0,
    total_marks INT NOT NULL DEFAULT 0,
    percentage DOUBLE NOT NULL DEFAULT 0.0,
    passed BOOLEAN NOT NULL DEFAULT FALSE,
    started_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP NULL,
    duration_seconds INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS assessment_attempt_answers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    attempt_id BIGINT NOT NULL,
    question_id BIGINT NOT NULL,
    selected_option VARCHAR(5),
    correct_option VARCHAR(5) NOT NULL,
    is_correct BOOLEAN NOT NULL DEFAULT FALSE,
    mistake_category VARCHAR(50),
    topic VARCHAR(100),
    category VARCHAR(50),
    FOREIGN KEY (attempt_id) REFERENCES assessment_attempts(id) ON DELETE CASCADE,
    FOREIGN KEY (question_id) REFERENCES questions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS coding_challenges (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    slug VARCHAR(150) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    topic VARCHAR(100) NOT NULL,
    difficulty VARCHAR(20) NOT NULL DEFAULT 'EASY',
    description LONGTEXT NOT NULL,
    example_input TEXT,
    example_output TEXT,
    constraints TEXT,
    hints TEXT,
    expected_approach TEXT,
    time_complexity VARCHAR(100) DEFAULT 'O(n)',
    space_complexity VARCHAR(100) DEFAULT 'O(1)',
    starter_code_java LONGTEXT,
    starter_code_js LONGTEXT,
    test_cases_json LONGTEXT
);

CREATE TABLE IF NOT EXISTS coding_submissions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    challenge_id BIGINT NOT NULL,
    language VARCHAR(30) NOT NULL DEFAULT 'java',
    code_content LONGTEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACCEPTED',
    test_cases_passed INT NOT NULL DEFAULT 0,
    total_test_cases INT NOT NULL DEFAULT 0,
    submitted_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (challenge_id) REFERENCES coding_challenges(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS placement_missions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    duration_days INT NOT NULL DEFAULT 7,
    category VARCHAR(50),
    icon VARCHAR(50) DEFAULT 'trophy',
    order_index INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS mission_tasks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    mission_id BIGINT NOT NULL,
    day_number INT NOT NULL DEFAULT 1,
    task_title VARCHAR(150) NOT NULL,
    task_description TEXT,
    task_type VARCHAR(20) NOT NULL DEFAULT 'LESSON',
    target_reference_id BIGINT,
    order_index INT DEFAULT 0,
    FOREIGN KEY (mission_id) REFERENCES placement_missions(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS student_mission_progress (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    mission_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NOT_STARTED',
    current_day INT NOT NULL DEFAULT 1,
    completed_tasks_count INT NOT NULL DEFAULT 0,
    total_tasks_count INT NOT NULL DEFAULT 0,
    started_at TIMESTAMP NULL,
    last_activity_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    UNIQUE KEY uk_user_mission (user_id, mission_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mission_id) REFERENCES placement_missions(id) ON DELETE CASCADE
);
