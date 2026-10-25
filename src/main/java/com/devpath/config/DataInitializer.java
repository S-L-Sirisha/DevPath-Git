package com.devpath.config;

import com.devpath.entity.*;
import com.devpath.entity.enums.*;
import com.devpath.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final LearningPathRepository pathRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final CodingChallengeRepository challengeRepository;
    private final PlacementMissionRepository missionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           LearningPathRepository pathRepository,
                           LessonRepository lessonRepository,
                           QuestionRepository questionRepository,
                           AssessmentRepository assessmentRepository,
                           AssessmentAttemptRepository attemptRepository,
                           CodingChallengeRepository challengeRepository,
                           PlacementMissionRepository missionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.pathRepository = pathRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.assessmentRepository = assessmentRepository;
        this.attemptRepository = attemptRepository;
        this.challengeRepository = challengeRepository;
        this.missionRepository = missionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() > 0) {
            logger.info("Database already seeded with initial records. Skipping seed step.");
            return;
        }

        logger.info("Seeding DevPath database with users, lessons, questions, challenges, and missions...");

        // 1. Seed Users
        User admin = new User(
                "Professor Admin",
                "admin@devpath.com",
                passwordEncoder.encode("Admin@123"),
                Role.ROLE_ADMIN,
                "DevOps & Systems Architect",
                "Department of Computer Science"
        );
        userRepository.save(admin);

        User student = new User(
                "Alex Rivera",
                "student@devpath.com",
                passwordEncoder.encode("Student@123"),
                Role.ROLE_STUDENT,
                "Full Stack Software Engineer",
                "School of Information Technology"
        );
        student = userRepository.save(student);

        // 2. Seed Learning Paths
        LearningPath pathJava = new LearningPath(
                "Full-Stack Java Placement Roadmap",
                "java-placement-roadmap",
                "Master modern Java, Spring Boot, OOP, and enterprise software engineering principles.",
                SkillCategory.PROGRAMMING_FUNDAMENTALS,
                Difficulty.BEGINNER,
                16,
                "code",
                1
        );
        pathJava = pathRepository.save(pathJava);

        LearningPath pathDsa = new LearningPath(
                "DSA Masterclass for Product Companies",
                "dsa-masterclass",
                "In-depth analysis of Arrays, Trees, Graphs, Dynamic Programming, and complexity analysis.",
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                Difficulty.INTERMEDIATE,
                24,
                "cpu",
                2
        );
        pathDsa = pathRepository.save(pathDsa);

        LearningPath pathCs = new LearningPath(
                "Core Computer Science Essentials",
                "core-cs-essentials",
                "Crucial Operating Systems, Database Management Systems, and Computer Networks concepts for technical interviews.",
                SkillCategory.CS_FUNDAMENTALS,
                Difficulty.INTERMEDIATE,
                14,
                "database",
                3
        );
        pathCs = pathRepository.save(pathCs);

        LearningPath pathAptitude = new LearningPath(
                "Campus Aptitude & Reasoning Sprint",
                "campus-aptitude-sprint",
                "Quantitative problem solving, syllogisms, logic deductions, and speed math techniques.",
                SkillCategory.APTITUDE_AND_LOGICAL,
                Difficulty.BEGINNER,
                10,
                "compass",
                4
        );
        pathAptitude = pathRepository.save(pathAptitude);

        LearningPath pathTechInterview = new LearningPath(
                "Cracking the Technical Coding Interview",
                "cracking-technical-interview",
                "Behavioral responses, system design basics, code refactoring, and mock scenario handling.",
                SkillCategory.TECH_INTERVIEW_PREP,
                Difficulty.ADVANCED,
                12,
                "briefcase",
                5
        );
        pathTechInterview = pathRepository.save(pathTechInterview);

        // 3. Seed Lessons
        Lesson lesson1 = new Lesson(
                pathJava,
                "Java Memory Model: Heap vs Stack & Garbage Collection",
                "java-memory-heap-stack-gc",
                SkillCategory.PROGRAMMING_FUNDAMENTALS,
                "Java Memory Management",
                Difficulty.EASY,
                "Understand where objects and primitives live in JVM memory, reference passing, and how GC frees unreachable objects.",
                """
                ### JVM Runtime Memory Structure
                The Java Virtual Machine organizes memory into two primary regions:
                
                1. **Stack Memory**: Fast, thread-local allocation for method frames, local primitive variables, and references to objects. Memory is automatically cleaned up when the method scope exits.
                2. **Heap Memory**: Shared pool where all instantiated objects reside. Garbage collection monitors and reclaims objects that are no longer strongly reachable.
                
                ```java
                public class MemoryDemo {
                    public static void main(String[] args) {
                        int primitiveAge = 21; // Stored directly on Stack
                        String name = new String("DevPath"); // 'name' reference on Stack, Object in Heap
                        process(name);
                    }
                    
                    static void process(String ref) {
                        System.out.println("Processing: " + ref);
                    }
                }
                ```
                
                #### Key Placement Gotchas:
                - Java is strictly **pass-by-value**. Object references are copied by value!
                - Memory leaks can occur in Java when static collections retain references to unused objects.
                """,
                "Basic Java syntax and class creation",
                "Stack is thread-safe and fast; Heap holds all objects; GC automates reclamation but static leaks can still occur.",
                12,
                1
        );
        lessonRepository.save(lesson1);

        Lesson lesson2 = new Lesson(
                pathDsa,
                "Optimal Sliding Window & Two Pointer Techniques",
                "sliding-window-two-pointers",
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "Arrays & Strings",
                Difficulty.MEDIUM,
                "Transform brute-force O(n²) contiguous subarray problems into linear O(n) solutions using two pointers.",
                """
                ### The Sliding Window Pattern
                The sliding window technique avoids redundant inner loops when finding maximums, minimums, or conditions within contiguous subarrays.
                
                ```java
                // Find maximum sum subarray of fixed size k in O(n)
                public int maxSubArraySum(int[] arr, int k) {
                    int n = arr.length;
                    if (n < k) return -1;
                    
                    int windowSum = 0;
                    for (int i = 0; i < k; i++) {
                        windowSum += arr[i];
                    }
                    
                    int maxSum = windowSum;
                    for (int i = k; i < n; i++) {
                        windowSum += arr[i] - arr[i - k]; // Slide: add incoming, remove outgoing
                        maxSum = Math.max(maxSum, windowSum);
                    }
                    return maxSum;
                }
                ```
                
                #### Complexity:
                - Time Complexity: O(n) single pass
                - Space Complexity: O(1) auxiliary variables
                """,
                "Array indexing and basic loop iterations",
                "Subarray problems often yield to sliding window; slide by adding right element and dropping left element.",
                15,
                2
        );
        lessonRepository.save(lesson2);

        Lesson lesson3 = new Lesson(
                pathCs,
                "Database Indexing & B-Trees: How Queries Run 100x Faster",
                "database-indexing-b-trees",
                SkillCategory.CS_FUNDAMENTALS,
                "DBMS & Indexing",
                Difficulty.MEDIUM,
                "Deep dive into clustered vs non-clustered indexes, B+ Trees, and EXPLAIN query plan analysis.",
                """
                ### What is a Database Index?
                Without an index, the database engine must execute a full table scan O(n). A B+ Tree index structures data in balanced pages, enabling search in logarithmic time O(log n).
                
                - **Clustered Index**: Dictates physical ordering of rows on disk (only 1 per table, usually Primary Key).
                - **Non-Clustered Index**: Separate search tree pointing back to the clustered index or physical tuple.
                
                ```sql
                -- Inspect query execution plan
                EXPLAIN ANALYZE SELECT * FROM users WHERE email = 'student@devpath.com';
                
                -- Create composite index for compound filters
                CREATE INDEX idx_user_status ON users(role, created_at);
                ```
                """,
                "Basic SQL SELECT statements",
                "Clustered index dictates physical disk storage; non-clustered indexes use pointers; avoid indexing low-cardinality columns.",
                14,
                3
        );
        lessonRepository.save(lesson3);

        // 4. Seed Questions Bank
        Question q1 = new Question(
                SkillCategory.PROGRAMMING_FUNDAMENTALS,
                "Java Memory & Reference Passing",
                Difficulty.EASY,
                "What is the output of the following Java snippet?\n```java\nvoid modify(StringBuilder sb) {\n    sb.append(\"Path\");\n    sb = new StringBuilder(\"New\");\n}\n// in main:\nStringBuilder s = new StringBuilder(\"Dev\");\nmodify(s);\nSystem.out.println(s);\n```",
                "StringBuilder s = new StringBuilder(\"Dev\");\nmodify(s);",
                "Dev",
                "DevPath",
                "New",
                "DevPathNew",
                "B",
                "Java passes object references by value. In `modify`, `sb.append(\"Path\")` mutates the existing object on the heap that `s` references. Reassigning `sb = new StringBuilder(\"New\")` changes the local parameter copy only, leaving the caller's reference `s` pointing to 'DevPath'.",
                MistakeCategory.CONCEPT_MISUNDERSTANDING,
                "Pass-By-Value & Reference Mutation in Java",
                "Always remember: reassigning a parameter does not change the caller's variable, but calling a mutating method on that reference modifies the shared object."
        );
        questionRepository.save(q1);

        Question q2 = new Question(
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "Arrays & Hash Maps",
                Difficulty.MEDIUM,
                "In the classic Two-Sum problem (finding two indices that sum to target), what is the time and space complexity of the optimal single-pass Hash Map approach?",
                null,
                "O(n²) Time, O(1) Space",
                "O(n log n) Time, O(n) Space",
                "O(n) Time, O(n) Space",
                "O(n) Time, O(1) Space",
                "C",
                "By iterating through the array once and storing previously seen numbers with their indices in a Hash Map, lookup for `target - current` takes O(1) average time. This gives O(n) overall time with O(n) space for the Hash Map.",
                MistakeCategory.TIME_SPACE_INEFFICIENCY,
                "Hash Map Time-Space Trade-offs",
                "Trading memory space for runtime speed is the cornerstone of optimal placement algorithms."
        );
        questionRepository.save(q2);

        Question q3 = new Question(
                SkillCategory.CS_FUNDAMENTALS,
                "Operating Systems & Concurrency",
                Difficulty.MEDIUM,
                "Which of the following conditions is NOT one of Coffman's four conditions required for a Deadlock to occur?",
                null,
                "Mutual Exclusion",
                "Hold and Wait",
                "Preemption Allowed",
                "Circular Wait",
                "C",
                "Coffman's four conditions are: 1) Mutual Exclusion, 2) Hold and Wait, 3) NO Preemption (resources cannot be forcibly reclaimed), and 4) Circular Wait. If preemption is allowed, deadlock cannot occur because the OS can take back resources.",
                MistakeCategory.CONCEPT_MISUNDERSTANDING,
                "Deadlock Prevention & Coffman Conditions",
                "Remember: 'No Preemption' is the requirement. Allowing preemption breaks the deadlock cycle."
        );
        questionRepository.save(q3);

        Question q4 = new Question(
                SkillCategory.APTITUDE_AND_LOGICAL,
                "Quantitative Aptitude - Work & Time",
                Difficulty.EASY,
                "If Person A can finish a software task in 6 days and Person B can finish the same task in 12 days, how many days will they take working together?",
                null,
                "4 days",
                "3 days",
                "5 days",
                "4.5 days",
                "A",
                "A's 1-day work = 1/6. B's 1-day work = 1/12. Combined 1-day work = 1/6 + 1/12 = 3/12 = 1/4. Therefore, working together they finish in 4 days.",
                MistakeCategory.INCORRECT_LOGIC,
                "Work and Time Reciprocal Formula",
                "Add the individual rate of work per day (reciprocal of days), then invert the sum to get total combined days."
        );
        questionRepository.save(q4);

        Question q5 = new Question(
                SkillCategory.TECH_INTERVIEW_PREP,
                "System Design & REST Architecture",
                Difficulty.MEDIUM,
                "Which HTTP method should be used for updating a resource such that multiple identical requests produce the same state without unintended side-effects (Idempotent)?",
                null,
                "POST",
                "PUT",
                "PATCH only",
                "CONNECT",
                "B",
                "PUT is defined by HTTP specification as idempotent: sending 1 PUT request or 10 identical PUT requests will result in the exact same resource state on the server. POST is non-idempotent because multiple calls create multiple records.",
                MistakeCategory.SYNTAX_OR_TYPE_TRAP,
                "HTTP Verbs & Idempotency in REST APIs",
                "Interviewers frequently ask about Idempotency. GET, PUT, and DELETE are idempotent; POST is not."
        );
        questionRepository.save(q5);

        Question q6 = new Question(
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "Binary Search & Edge Cases",
                Difficulty.EASY,
                "In Binary Search, what is the standard recommended calculation for the middle index to prevent 32-bit signed integer overflow in languages like Java or C++?",
                "int mid = (low + high) / 2; // Can this overflow?",
                "mid = (low + high) / 2",
                "mid = low + (high - low) / 2",
                "mid = (high - low) / 2",
                "mid = high + (low / 2)",
                "B",
                "If `low + high` exceeds `Integer.MAX_VALUE` (2,147,483,647), it overflows into a negative number, leading to an ArrayIndexOutOfBoundsException. Calculating `low + (high - low) / 2` is mathematically identical and guarantees no overflow.",
                MistakeCategory.BOUNDARY_CASE_ERROR,
                "Integer Overflow in Binary Search",
                "This boundary case bug famously went unnoticed in Java's standard library for 9 years until highlighted by Joshua Bloch."
        );
        questionRepository.save(q6);

        // 5. Seed Assessment
        Assessment mockAssessment = new Assessment(
                "Placement Diagnostic Readiness Assessment",
                "Comprehensive 6-question benchmark evaluating Programming Fundamentals, Data Structures, Aptitude, OS, and Technical Interview principles.",
                null, // Multi-category benchmark
                15,
                60,
                6,
                Difficulty.MEDIUM,
                true
        );
        mockAssessment.setQuestions(new ArrayList<>(List.of(q1, q2, q3, q4, q5, q6)));
        mockAssessment = assessmentRepository.save(mockAssessment);

        // 6. Seed Coding Challenges
        CodingChallenge challenge1 = new CodingChallenge(
                "Two Sum — Hash Map Optimization",
                "two-sum",
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "Arrays & Hashing",
                Difficulty.EASY,
                """
                Given an array of integers `nums` and an integer `target`, return indices of the two numbers such that they add up to `target`.
                You may assume that each input would have exactly one solution, and you may not use the same element twice.
                """,
                "nums = [2, 7, 11, 15], target = 9",
                "[0, 1]",
                "2 <= nums.length <= 10^4, -10^9 <= nums[i] <= 10^9",
                "Use a HashMap to store values you have already inspected. For each element x, check if (target - x) exists in the map.",
                "Single pass hash table: iterate through array, compute complement = target - nums[i]. Check if complement is in map; if yes, return indices; otherwise put nums[i] -> index in map.",
                "O(n)",
                "O(n)",
                """
                class Solution {
                    public int[] twoSum(int[] nums, int target) {
                        Map<Integer, Integer> map = new HashMap<>();
                        for (int i = 0; i < nums.length; i++) {
                            int complement = target - nums[i];
                            if (map.containsKey(complement)) {
                                return new int[] { map.get(complement), i };
                            }
                            map.put(nums[i], i);
                        }
                        return new int[0];
                    }
                }
                """,
                """
                function twoSum(nums, target) {
                    const map = new Map();
                    for (let i = 0; i < nums.length; i++) {
                        const complement = target - nums[i];
                        if (map.has(complement)) {
                            return [map.get(complement), i];
                        }
                        map.set(nums[i], i);
                    }
                    return [];
                }
                """,
                "[{\"input\":\"nums = [2,7,11,15], target = 9\",\"expectedOutput\":\"[0, 1]\"},{\"input\":\"nums = [3,2,4], target = 6\",\"expectedOutput\":\"[1, 2]\"}]"
        );
        challengeRepository.save(challenge1);

        CodingChallenge challenge2 = new CodingChallenge(
                "Reverse a Singly Linked List",
                "reverse-linked-list",
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "Linked Lists",
                Difficulty.EASY,
                """
                Given the head of a singly linked list, reverse the list, and return the reversed list.
                Solve iteratively in O(n) time and O(1) space.
                """,
                "head = [1, 2, 3, 4, 5]",
                "[5, 4, 3, 2, 1]",
                "Number of nodes in the list is in the range [0, 5000]. -5000 <= Node.val <= 5000",
                "Maintain three pointers: prev = null, curr = head, next = null. In each step, save curr.next, flip curr.next to prev, then advance prev and curr.",
                "Iterative pointer reversal: in each loop step, flip curr.next to prev, advance prev to curr, and advance curr to next.",
                "O(n)",
                "O(1)",
                """
                class Solution {
                    public ListNode reverseList(ListNode head) {
                        ListNode prev = null;
                        ListNode curr = head;
                        while (curr != null) {
                            ListNode nextTemp = curr.next;
                            curr.next = prev;
                            prev = curr;
                            curr = nextTemp;
                        }
                        return prev;
                    }
                }
                """,
                """
                function reverseList(head) {
                    let prev = null;
                    let curr = head;
                    while (curr !== null) {
                        let nextTemp = curr.next;
                        curr.next = prev;
                        prev = curr;
                        curr = nextTemp;
                    }
                    return prev;
                }
                """,
                "[{\"input\":\"head = [1,2,3,4,5]\",\"expectedOutput\":\"[5, 4, 3, 2, 1]\"}]"
        );
        challengeRepository.save(challenge2);

        // 7. Seed Placement Missions
        PlacementMission mission1 = new PlacementMission(
                "7-Day DSA Placement Sprint",
                "A focused 7-day challenge covering Arrays, Strings, Hash Maps, Linked Lists, Stacks, Trees, and Dynamic Programming.",
                7,
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS,
                "trophy",
                1
        );
        mission1 = missionRepository.save(mission1);

        MissionTask t1 = new MissionTask(mission1, 1, "Review Sliding Window Lesson", "Complete reading the sliding window masterclass.", TaskType.LESSON, lesson2.getId(), 1);
        MissionTask t2 = new MissionTask(mission1, 1, "Solve Two Sum Challenge", "Submit optimal O(n) hash map solution.", TaskType.CODING, challenge1.getId(), 2);
        MissionTask t3 = new MissionTask(mission1, 2, "Reverse Linked List Challenge", "Complete iterative O(1) space reversal.", TaskType.CODING, challenge2.getId(), 1);
        MissionTask t4 = new MissionTask(mission1, 3, "Placement Diagnostic Mock", "Take the timed assessment to evaluate weak topics.", TaskType.QUIZ, mockAssessment.getId(), 1);
        mission1.setTasks(new ArrayList<>(List.of(t1, t2, t3, t4)));
        missionRepository.save(mission1);

        PlacementMission mission2 = new PlacementMission(
                "Java & Backend Interview Mastery",
                "Deep dive into JVM memory, multithreading, Spring Boot REST architecture, and SQL query tuning.",
                5,
                SkillCategory.PROGRAMMING_FUNDAMENTALS,
                "server",
                2
        );
        mission2 = missionRepository.save(mission2);
        MissionTask m2t1 = new MissionTask(mission2, 1, "Review JVM Memory Model", "Read Heap vs Stack lesson.", TaskType.LESSON, lesson1.getId(), 1);
        MissionTask m2t2 = new MissionTask(mission2, 2, "Database Indexing Deep Dive", "Study B+ Tree indexing mechanics.", TaskType.LESSON, lesson3.getId(), 1);
        mission2.setTasks(new ArrayList<>(List.of(m2t1, m2t2)));
        missionRepository.save(mission2);

        // 8. Seed Initial Baseline Assessment Attempt for Demo Student (Alex Rivera)
        // This gives real stored data so radar charts, mistake tracker, and proof of progress light up with authentic numbers!
        AssessmentAttempt baseline = new AssessmentAttempt(student, mockAssessment);
        baseline.setScore(4);
        baseline.setTotalMarks(6);
        baseline.setPercentage(66.7);
        baseline.setPassed(true);
        baseline.setDurationSeconds(420);
        baseline.setStartedAt(LocalDateTime.now().minusDays(5));
        baseline.setCompletedAt(LocalDateTime.now().minusDays(5).plusMinutes(7));

        List<AssessmentAttemptAnswer> baselineAnswers = new ArrayList<>();
        // Correct answers: q1, q2, q4, q6
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q1, "B", "B", true, null, q1.getTopic(), q1.getCategory()));
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q2, "C", "C", true, null, q2.getTopic(), q2.getCategory()));
        // Incorrect answer: q3 (Deadlock Coffman condition - selected A instead of C)
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q3, "A", "C", false, MistakeCategory.CONCEPT_MISUNDERSTANDING, q3.getTopic(), q3.getCategory()));
        // Correct answer: q4
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q4, "A", "A", true, null, q4.getTopic(), q4.getCategory()));
        // Incorrect answer: q5 (HTTP Put idempotency - selected A instead of B)
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q5, "A", "B", false, MistakeCategory.SYNTAX_OR_TYPE_TRAP, q5.getTopic(), q5.getCategory()));
        // Correct answer: q6
        baselineAnswers.add(new AssessmentAttemptAnswer(baseline, q6, "B", "B", true, null, q6.getTopic(), q6.getCategory()));

        baseline.setAnswers(baselineAnswers);
        attemptRepository.save(baseline);

        // Seed a second attempt (showing progress improvement!)
        AssessmentAttempt improved = new AssessmentAttempt(student, mockAssessment);
        improved.setScore(5);
        improved.setTotalMarks(6);
        improved.setPercentage(83.3);
        improved.setPassed(true);
        improved.setDurationSeconds(350);
        improved.setStartedAt(LocalDateTime.now().minusDays(1));
        improved.setCompletedAt(LocalDateTime.now().minusDays(1).plusMinutes(6));

        List<AssessmentAttemptAnswer> improvedAnswers = new ArrayList<>();
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q1, "B", "B", true, null, q1.getTopic(), q1.getCategory()));
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q2, "C", "C", true, null, q2.getTopic(), q2.getCategory()));
        // Q3 now answered correctly! (Progress!)
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q3, "C", "C", true, null, q3.getTopic(), q3.getCategory()));
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q4, "A", "A", true, null, q4.getTopic(), q4.getCategory()));
        // Q5 still needs revision
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q5, "A", "B", false, MistakeCategory.SYNTAX_OR_TYPE_TRAP, q5.getTopic(), q5.getCategory()));
        improvedAnswers.add(new AssessmentAttemptAnswer(improved, q6, "B", "B", true, null, q6.getTopic(), q6.getCategory()));

        improved.setAnswers(improvedAnswers);
        attemptRepository.save(improved);

        logger.info("DevPath seed data initialization completed successfully.");
    }
}
