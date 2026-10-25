package com.devpath;

import com.devpath.dto.AdaptiveRecommendationDto;
import com.devpath.dto.MistakeDetailDto;
import com.devpath.dto.ProofOfProgressDto;
import com.devpath.dto.RadarScoreDto;
import com.devpath.entity.*;
import com.devpath.entity.enums.Difficulty;
import com.devpath.entity.enums.MistakeCategory;
import com.devpath.entity.enums.SkillCategory;
import com.devpath.repository.*;
import com.devpath.service.DiagnosticService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticServiceTest {

    @Mock
    private AssessmentAttemptAnswerRepository answerRepository;

    @Mock
    private AssessmentAttemptRepository attemptRepository;

    @Mock
    private StudentLessonProgressRepository progressRepository;

    @Mock
    private CodingSubmissionRepository submissionRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private DiagnosticService diagnosticService;

    @Test
    @DisplayName("Radar calculation computes scores per skill category from real assessment answers")
    void testCalculateRadarScoresWithRealData() {
        Long userId = 1L;
        List<AssessmentAttemptAnswer> answers = new ArrayList<>();

        Question qProg = new Question();
        qProg.setId(1L);
        qProg.setCategory(SkillCategory.PROGRAMMING_FUNDAMENTALS);

        Question qDsa = new Question();
        qDsa.setId(2L);
        qDsa.setCategory(SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS);

        AssessmentAttempt attempt = new AssessmentAttempt();

        // 2 Programming answers: 2 correct (100%)
        answers.add(new AssessmentAttemptAnswer(attempt, qProg, "A", "A", true, null, "Java", SkillCategory.PROGRAMMING_FUNDAMENTALS));
        answers.add(new AssessmentAttemptAnswer(attempt, qProg, "B", "B", true, null, "Java", SkillCategory.PROGRAMMING_FUNDAMENTALS));

        // 2 DSA answers: 1 correct, 1 wrong (50%)
        answers.add(new AssessmentAttemptAnswer(attempt, qDsa, "A", "A", true, null, "Trees", SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS));
        answers.add(new AssessmentAttemptAnswer(attempt, qDsa, "B", "C", false, MistakeCategory.BOUNDARY_CASE_ERROR, "Trees", SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS));

        when(answerRepository.findByUserId(userId)).thenReturn(answers);

        RadarScoreDto radar = diagnosticService.calculateRadarScores(userId);

        assertNotNull(radar);
        assertFalse(radar.getIsDemoData());
        assertEquals(100.0, radar.getProgrammingFundamentals());
        assertEquals(50.0, radar.getDataStructuresAndAlgorithms());
        assertEquals(4, radar.getTotalQuestionsAttempted());
        assertEquals(3, radar.getTotalCorrect());
        assertEquals("Programming Fundamentals", radar.getStrongestSkill());
    }

    @Test
    @DisplayName("Proof of progress calculates exact percentage point difference between initial baseline and latest assessment")
    void testCalculateProofOfProgressDelta() {
        Long userId = 1L;
        User user = new User();
        user.setId(userId);

        Assessment mockAssessment = new Assessment();
        mockAssessment.setTitle("Placement Mock");

        AssessmentAttempt baseline = new AssessmentAttempt(user, mockAssessment);
        baseline.setPercentage(40.0);
        baseline.setStartedAt(LocalDateTime.now().minusDays(10));

        AssessmentAttempt latest = new AssessmentAttempt(user, mockAssessment);
        latest.setPercentage(75.0);
        latest.setStartedAt(LocalDateTime.now());
        latest.setCompletedAt(LocalDateTime.now().plusMinutes(10));

        when(attemptRepository.findByUserIdOrderByStartedAtDesc(userId)).thenReturn(List.of(latest, baseline));
        when(progressRepository.countByUserIdAndCompletedTrue(userId)).thenReturn(5L);
        when(submissionRepository.countByUserIdAndStatus(userId, "ACCEPTED")).thenReturn(8L);

        ProofOfProgressDto proof = diagnosticService.calculateProofOfProgress(userId);

        assertNotNull(proof);
        assertFalse(proof.getIsDemoData());
        assertEquals(40.0, proof.getInitialScorePercentage());
        assertEquals(75.0, proof.getCurrentScorePercentage());
        // 75.0 - 40.0 = 35.0 percentage points delta
        assertEquals(35.0, proof.getPercentagePointsDelta());
        assertEquals(5L, proof.getTotalLessonsCompleted());
        assertEquals(8L, proof.getTotalChallengesSolved());
        assertEquals(2L, proof.getTotalAssessmentsTaken());
    }

    @Test
    @DisplayName("Adaptive recommendation engine identifies weak topics below 65% benchmark and explains why")
    void testAdaptiveRecommendationsReasoning() {
        Long userId = 1L;
        // Topic 'Binary Trees' has 3 attempts, 1 correct (33.3% accuracy)
        Object[] row = new Object[]{"Binary Trees", 3L, 1L};
        when(answerRepository.getTopicStatsByUserId(userId)).thenReturn(List.<Object[]>of(row));

        LearningPath path = new LearningPath();
        Lesson lesson = new Lesson(path, "Binary Tree Traversals", "binary-tree-traversals",
                SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS, "Binary Trees", Difficulty.EASY,
                "Summary", "Content", "Prerequisites", "Takeaways", 15, 1);
        lesson.setId(22L);

        when(lessonRepository.findByTopicContainingIgnoreCaseAndPublishedTrue("Binary Trees"))
                .thenReturn(List.of(lesson));

        List<AdaptiveRecommendationDto> recs = diagnosticService.generateAdaptiveRecommendations(userId);

        assertNotNull(recs);
        assertFalse(recs.isEmpty());
        AdaptiveRecommendationDto top = recs.get(0);
        assertEquals("LESSON", top.getType());
        assertEquals("Binary Trees", top.getTopic());
        assertTrue(top.getReason().contains("accuracy in 'Binary Trees' is only 33%"));
        assertTrue(top.getReason().contains("below 65% placement benchmark"));
    }

    @Test
    @DisplayName("Mistake tracker categorizes errors into diagnostic buckets with revision topics")
    void testMistakeTrackerCategorization() {
        Long userId = 1L;
        Question q = new Question();
        q.setId(55L);
        q.setQuestionText("What is the middle element calculation?");
        q.setOptionA("low + high / 2");
        q.setOptionB("low + (high - low) / 2");
        q.setCorrectOption("B");
        q.setExplanation("Prevents 32-bit signed integer overflow.");
        q.setMistakeCategory(MistakeCategory.BOUNDARY_CASE_ERROR);
        q.setTopic("Binary Search");
        q.setCategory(SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS);
        q.setRevisionTopic("Integer Overflow in Array Indexing");
        q.setFollowUpTip("Always use low + (high - low) / 2");

        AssessmentAttemptAnswer mistakeAns = new AssessmentAttemptAnswer(
                new AssessmentAttempt(), q, "A", "B", false, MistakeCategory.BOUNDARY_CASE_ERROR, "Binary Search", SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS
        );

        when(answerRepository.findMistakesByUserId(userId)).thenReturn(List.of(mistakeAns));

        List<MistakeDetailDto> mistakes = diagnosticService.getMistakesByStudent(userId);

        assertEquals(1, mistakes.size());
        MistakeDetailDto m = mistakes.get(0);
        assertEquals("BOUNDARY_CASE_ERROR", m.getMistakeCategory());
        assertEquals("Boundary-Case Error", m.getMistakeCategoryLabel());
        assertEquals("Integer Overflow in Array Indexing", m.getRevisionTopic());
        assertEquals("Prevents 32-bit signed integer overflow.", m.getExplanation());
    }
}
