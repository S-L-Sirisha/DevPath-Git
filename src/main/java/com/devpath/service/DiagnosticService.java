package com.devpath.service;

import com.devpath.dto.AdaptiveRecommendationDto;
import com.devpath.dto.MistakeDetailDto;
import com.devpath.dto.ProofOfProgressDto;
import com.devpath.dto.RadarScoreDto;
import com.devpath.entity.*;
import com.devpath.entity.enums.SkillCategory;
import com.devpath.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DiagnosticService {

    private final AssessmentAttemptAnswerRepository answerRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final StudentLessonProgressRepository progressRepository;
    private final CodingSubmissionRepository submissionRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;

    public DiagnosticService(AssessmentAttemptAnswerRepository answerRepository,
                             AssessmentAttemptRepository attemptRepository,
                             StudentLessonProgressRepository progressRepository,
                             CodingSubmissionRepository submissionRepository,
                             LessonRepository lessonRepository,
                             QuestionRepository questionRepository) {
        this.answerRepository = answerRepository;
        this.attemptRepository = attemptRepository;
        this.progressRepository = progressRepository;
        this.submissionRepository = submissionRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
    }

    @Transactional(readOnly = true)
    public RadarScoreDto calculateRadarScores(Long userId) {
        List<AssessmentAttemptAnswer> userAnswers = answerRepository.findByUserId(userId);
        RadarScoreDto radar = new RadarScoreDto();

        if (userAnswers.isEmpty()) {
            // Student has not yet completed any assessments.
            // Provide a clearly labeled baseline demo state as requested by prompt requirements
            radar.setIsDemoData(true);
            radar.setProgrammingFundamentals(45.0);
            radar.setDataStructuresAndAlgorithms(35.0);
            radar.setAptitudeAndLogical(50.0);
            radar.setCsFundamentals(40.0);
            radar.setTechInterviewPrep(30.0);
            radar.setOverallReadinessIndex(40.0);
            radar.setStrongestSkill("Aptitude & Logical Reasoning (Sample Baseline)");
            radar.setWeakestSkill("Technical Interview Preparation (Sample Baseline)");
            radar.setAreasRequiringImprovement(List.of("Data Structures & Algorithms", "Technical Interview Prep", "CS Fundamentals"));
            radar.setRecommendedNextActivity("Take your first Placement Diagnostic Assessment to replace this baseline with real performance data.");
            return radar;
        }

        radar.setIsDemoData(false);
        radar.setTotalQuestionsAttempted(userAnswers.size());

        int totalCorrect = 0;
        Map<SkillCategory, Integer> categoryTotals = new EnumMap<>(SkillCategory.class);
        Map<SkillCategory, Integer> categoryCorrect = new EnumMap<>(SkillCategory.class);

        for (SkillCategory cat : SkillCategory.values()) {
            categoryTotals.put(cat, 0);
            categoryCorrect.put(cat, 0);
        }

        for (AssessmentAttemptAnswer ans : userAnswers) {
            SkillCategory cat = ans.getCategory();
            if (cat != null) {
                categoryTotals.put(cat, categoryTotals.get(cat) + 1);
                if (Boolean.TRUE.equals(ans.getIsCorrect())) {
                    categoryCorrect.put(cat, categoryCorrect.get(cat) + 1);
                    totalCorrect++;
                }
            }
        }
        radar.setTotalCorrect(totalCorrect);

        // Calculate scores per category
        Map<SkillCategory, Double> categoryPercentages = new EnumMap<>(SkillCategory.class);
        Map<String, Integer> attemptsMap = new HashMap<>();

        for (SkillCategory cat : SkillCategory.values()) {
            int total = categoryTotals.get(cat);
            int correct = categoryCorrect.get(cat);
            double score = total > 0 ? Math.round(((double) correct / total) * 100.0 * 10.0) / 10.0 : 0.0;
            categoryPercentages.put(cat, score);
            attemptsMap.put(cat.name(), total);
        }
        radar.setCategoryAttempts(attemptsMap);

        radar.setProgrammingFundamentals(categoryPercentages.get(SkillCategory.PROGRAMMING_FUNDAMENTALS));
        radar.setDataStructuresAndAlgorithms(categoryPercentages.get(SkillCategory.DATA_STRUCTURES_AND_ALGORITHMS));
        radar.setAptitudeAndLogical(categoryPercentages.get(SkillCategory.APTITUDE_AND_LOGICAL));
        radar.setCsFundamentals(categoryPercentages.get(SkillCategory.CS_FUNDAMENTALS));
        radar.setTechInterviewPrep(categoryPercentages.get(SkillCategory.TECH_INTERVIEW_PREP));

        // Calculate weighted average
        double overall = categoryPercentages.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        radar.setOverallReadinessIndex(Math.round(overall * 10.0) / 10.0);

        // Find strongest and weakest
        SkillCategory strongest = null;
        SkillCategory weakest = null;
        double maxScore = -1.0;
        double minScore = 101.0;
        List<String> areasRequiringImprovement = new ArrayList<>();

        for (Map.Entry<SkillCategory, Double> entry : categoryPercentages.entrySet()) {
            if (entry.getValue() > maxScore) {
                maxScore = entry.getValue();
                strongest = entry.getKey();
            }
            if (entry.getValue() < minScore) {
                minScore = entry.getValue();
                weakest = entry.getKey();
            }
            if (entry.getValue() < 70.0) {
                areasRequiringImprovement.add(entry.getKey().getDisplayName() + " (" + entry.getValue() + "%)");
            }
        }

        radar.setStrongestSkill(strongest != null ? strongest.getDisplayName() : "N/A");
        radar.setWeakestSkill(weakest != null ? weakest.getDisplayName() : "N/A");
        radar.setAreasRequiringImprovement(areasRequiringImprovement);

        if (weakest != null) {
            radar.setRecommendedNextActivity("Prioritize review of " + weakest.getDisplayName() + " to elevate placement threshold.");
        } else {
            radar.setRecommendedNextActivity("Keep practicing full-length mock assessments.");
        }

        return radar;
    }

    @Transactional(readOnly = true)
    public List<AdaptiveRecommendationDto> generateAdaptiveRecommendations(Long userId) {
        List<AdaptiveRecommendationDto> recommendations = new ArrayList<>();
        List<Object[]> topicStats = answerRepository.getTopicStatsByUserId(userId);

        // Rule 1: Identify weak topics (accuracy < 65% with at least 1 attempt)
        for (Object[] row : topicStats) {
            String topic = (String) row[0];
            long total = (Long) row[1];
            long correct = (Long) row[2];
            double accuracy = total > 0 ? (double) correct / total : 0.0;

            if (accuracy < 0.65 && topic != null) {
                // Find relevant lesson for this topic
                List<Lesson> matchingLessons = lessonRepository.findByTopicContainingIgnoreCaseAndPublishedTrue(topic);
                if (!matchingLessons.isEmpty()) {
                    Lesson lesson = matchingLessons.get(0);
                    recommendations.add(new AdaptiveRecommendationDto(
                            "LESSON",
                            "Review Core Concept: " + lesson.getTitle(),
                            topic,
                            lesson.getCategory().name(),
                            lesson.getId(),
                            lesson.getSlug(),
                            String.format("Recommended because your accuracy in '%s' is only %.0f%% (below 65%% placement benchmark). Reviewing worked examples will fix logic gaps.", topic, accuracy * 100),
                            "HIGH"
                    ));
                }
            }
        }

        // Rule 2: If no weak topics found yet (or new user), recommend foundational lessons
        if (recommendations.isEmpty()) {
            List<Lesson> foundationalLessons = lessonRepository.findByPublishedTrue();
            if (!foundationalLessons.isEmpty()) {
                Lesson first = foundationalLessons.get(0);
                recommendations.add(new AdaptiveRecommendationDto(
                        "LESSON",
                        "Start Placement Foundation: " + first.getTitle(),
                        first.getTopic(),
                        first.getCategory().name(),
                        first.getId(),
                        first.getSlug(),
                        "Recommended as an essential stepping stone to establish your placement baseline.",
                        "MEDIUM"
                ));
            }
        }

        // Rule 3: Add targeted coding challenge recommendation
        recommendations.add(new AdaptiveRecommendationDto(
                "CODING_CHALLENGE",
                "Targeted Practice: Two Sum & Optimal Hash Maps",
                "Arrays & Hashing",
                "DATA_STRUCTURES_AND_ALGORITHMS",
                1L,
                "two-sum",
                "Technical interviews commonly screen candidates on O(n) hash map lookups. Solidify your pattern recognition.",
                "HIGH"
        ));

        return recommendations;
    }

    @Transactional(readOnly = true)
    public List<MistakeDetailDto> getMistakesByStudent(Long userId) {
        List<AssessmentAttemptAnswer> mistakeAnswers = answerRepository.findMistakesByUserId(userId);
        List<MistakeDetailDto> dtos = new ArrayList<>();

        for (AssessmentAttemptAnswer ans : mistakeAnswers) {
            Question q = ans.getQuestion();
            MistakeDetailDto dto = new MistakeDetailDto();
            dto.setQuestionId(q.getId());
            dto.setQuestionText(q.getQuestionText());
            dto.setCodeSnippet(q.getCodeSnippet());
            dto.setSelectedOption(ans.getSelectedOption());
            dto.setSelectedOptionText(getOptionText(q, ans.getSelectedOption()));
            dto.setCorrectOption(q.getCorrectOption());
            dto.setCorrectOptionText(getOptionText(q, q.getCorrectOption()));
            dto.setExplanation(q.getExplanation());
            dto.setMistakeCategory(ans.getMistakeCategory() != null ? ans.getMistakeCategory().name() : q.getMistakeCategory().name());
            dto.setMistakeCategoryLabel(ans.getMistakeCategory() != null ? ans.getMistakeCategory().getLabel() : q.getMistakeCategory().getLabel());
            dto.setMistakeDescription(ans.getMistakeCategory() != null ? ans.getMistakeCategory().getDescription() : q.getMistakeCategory().getDescription());
            dto.setTopic(q.getTopic());
            dto.setCategory(q.getCategory().getDisplayName());
            dto.setRevisionTopic(q.getRevisionTopic());
            dto.setFollowUpTip(q.getFollowUpTip());
            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    public ProofOfProgressDto calculateProofOfProgress(Long userId) {
        ProofOfProgressDto proof = new ProofOfProgressDto();
        List<AssessmentAttempt> attempts = attemptRepository.findByUserIdOrderByStartedAtDesc(userId);

        long lessonsCount = progressRepository.countByUserIdAndCompletedTrue(userId);
        long challengesCount = submissionRepository.countByUserIdAndStatus(userId, "ACCEPTED");
        proof.setTotalLessonsCompleted(lessonsCount);
        proof.setTotalChallengesSolved(challengesCount);
        proof.setTotalAssessmentsTaken((long) attempts.size());

        if (attempts.isEmpty()) {
            proof.setIsDemoData(true);
            proof.setInitialScorePercentage(40.0);
            proof.setCurrentScorePercentage(75.0);
            proof.setPercentagePointsDelta(35.0);
            proof.setInitialWeakAreas(List.of("Dynamic Memory Allocation", "Binary Trees & BST", "Deadlock Conditions"));
            proof.setCurrentStrengths(List.of("Array Manipulation", "OOP Principles", "Basic SQL Queries"));
            return proof;
        }

        proof.setIsDemoData(false);
        AssessmentAttempt latestAttempt = attempts.get(0);
        AssessmentAttempt baselineAttempt = attempts.get(attempts.size() - 1);

        proof.setLatestAssessmentDate(latestAttempt.getCompletedAt() != null ? latestAttempt.getCompletedAt() : latestAttempt.getStartedAt());
        proof.setBaselineAssessmentDate(baselineAttempt.getStartedAt());

        double initial = baselineAttempt.getPercentage();
        double current = latestAttempt.getPercentage();
        proof.setInitialScorePercentage(initial);
        proof.setCurrentScorePercentage(current);
        proof.setPercentagePointsDelta(Math.round((current - initial) * 10.0) / 10.0);

        // Derive weak and strong areas from actual answers
        List<Object[]> topicStats = answerRepository.getTopicStatsByUserId(userId);
        List<String> weak = new ArrayList<>();
        List<String> strong = new ArrayList<>();

        for (Object[] row : topicStats) {
            String topic = (String) row[0];
            long total = (Long) row[1];
            long correct = (Long) row[2];
            double acc = total > 0 ? (double) correct / total : 0.0;
            if (acc >= 0.70) {
                strong.add(topic + " (" + Math.round(acc * 100) + "%)");
            } else {
                weak.add(topic + " (" + Math.round(acc * 100) + "%)");
            }
        }

        proof.setInitialWeakAreas(weak.isEmpty() ? List.of("Under Evaluation") : weak);
        proof.setCurrentStrengths(strong.isEmpty() ? List.of("Under Evaluation") : strong);

        return proof;
    }

    private String getOptionText(Question q, String optionLetter) {
        if (optionLetter == null) return "None selected";
        return switch (optionLetter.toUpperCase().trim()) {
            case "A" -> q.getOptionA();
            case "B" -> q.getOptionB();
            case "C" -> q.getOptionC();
            case "D" -> q.getOptionD();
            default -> "Option " + optionLetter;
        };
    }
}
