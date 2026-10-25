package com.devpath.service;

import com.devpath.dto.AssessmentResultDto;
import com.devpath.dto.AssessmentSubmissionDto;
import com.devpath.dto.MistakeDetailDto;
import com.devpath.entity.Assessment;
import com.devpath.entity.AssessmentAttempt;
import com.devpath.entity.AssessmentAttemptAnswer;
import com.devpath.entity.Question;
import com.devpath.entity.User;
import com.devpath.exception.BadRequestException;
import com.devpath.exception.ResourceNotFoundException;
import com.devpath.repository.AssessmentAttemptAnswerRepository;
import com.devpath.repository.AssessmentAttemptRepository;
import com.devpath.repository.AssessmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class AssessmentService {

    private final AssessmentRepository assessmentRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final AssessmentAttemptAnswerRepository answerRepository;
    private final DiagnosticService diagnosticService;

    public AssessmentService(AssessmentRepository assessmentRepository,
                             AssessmentAttemptRepository attemptRepository,
                             AssessmentAttemptAnswerRepository answerRepository,
                             DiagnosticService diagnosticService) {
        this.assessmentRepository = assessmentRepository;
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.diagnosticService = diagnosticService;
    }

    public List<Assessment> getAllAssessments() {
        return assessmentRepository.findAll();
    }

    public Assessment getAssessmentById(Long id) {
        return assessmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Assessment not found with id: " + id));
    }

    @Transactional
    public AssessmentResultDto submitAssessment(User user, AssessmentSubmissionDto submission) {
        Assessment assessment = getAssessmentById(submission.getAssessmentId());
        List<Question> questions = assessment.getQuestions();

        if (questions == null || questions.isEmpty()) {
            throw new BadRequestException("Assessment has no configured questions.");
        }

        AssessmentAttempt attempt = new AssessmentAttempt(user, assessment);
        attempt.setDurationSeconds(submission.getDurationSeconds() != null ? submission.getDurationSeconds() : 0);
        attempt.setCompletedAt(LocalDateTime.now());
        attempt.setTotalMarks(questions.size());

        int score = 0;
        List<AssessmentAttemptAnswer> attemptAnswers = new ArrayList<>();
        Map<String, int[]> categoryTally = new HashMap<>(); // [total, correct]

        Map<Long, String> userAnswers = submission.getAnswers() != null ? submission.getAnswers() : Collections.emptyMap();

        for (Question q : questions) {
            String selectedOption = userAnswers.get(q.getId());
            boolean isCorrect = selectedOption != null && selectedOption.trim().equalsIgnoreCase(q.getCorrectOption().trim());

            if (isCorrect) {
                score++;
            }

            AssessmentAttemptAnswer ans = new AssessmentAttemptAnswer(
                    attempt,
                    q,
                    selectedOption != null ? selectedOption.trim().toUpperCase() : null,
                    q.getCorrectOption().trim().toUpperCase(),
                    isCorrect,
                    !isCorrect ? q.getMistakeCategory() : null,
                    q.getTopic(),
                    q.getCategory()
            );
            attemptAnswers.add(ans);

            String catName = q.getCategory().getDisplayName();
            categoryTally.putIfAbsent(catName, new int[]{0, 0});
            categoryTally.get(catName)[0]++;
            if (isCorrect) {
                categoryTally.get(catName)[1]++;
            }
        }

        attempt.setScore(score);
        double percentage = Math.round(((double) score / questions.size()) * 100.0 * 10.0) / 10.0;
        attempt.setPercentage(percentage);
        attempt.setPassed(percentage >= assessment.getPassingScorePercentage());
        attempt.setAnswers(attemptAnswers);

        attempt = attemptRepository.save(attempt);

        // Build result DTO
        AssessmentResultDto result = new AssessmentResultDto();
        result.setAttemptId(attempt.getId());
        result.setAssessmentId(assessment.getId());
        result.setAssessmentTitle(assessment.getTitle());
        result.setScore(score);
        result.setTotalMarks(questions.size());
        result.setPercentage(percentage);
        result.setPassed(attempt.getPassed());
        result.setDurationSeconds(attempt.getDurationSeconds());
        result.setCompletedAt(attempt.getCompletedAt());

        Map<String, Double> categoryBreakdown = new HashMap<>();
        for (Map.Entry<String, int[]> entry : categoryTally.entrySet()) {
            int total = entry.getValue()[0];
            int correct = entry.getValue()[1];
            double catPct = total > 0 ? Math.round(((double) correct / total) * 100.0 * 10.0) / 10.0 : 0.0;
            categoryBreakdown.put(entry.getKey(), catPct);
        }
        result.setCategoryBreakdown(categoryBreakdown);

        // Populate mistake details
        List<MistakeDetailDto> mistakes = new ArrayList<>();
        for (AssessmentAttemptAnswer ans : attemptAnswers) {
            if (!ans.getIsCorrect()) {
                Question q = ans.getQuestion();
                MistakeDetailDto mistake = new MistakeDetailDto();
                mistake.setQuestionId(q.getId());
                mistake.setQuestionText(q.getQuestionText());
                mistake.setCodeSnippet(q.getCodeSnippet());
                mistake.setSelectedOption(ans.getSelectedOption());
                mistake.setSelectedOptionText(getOptionLabel(q, ans.getSelectedOption()));
                mistake.setCorrectOption(q.getCorrectOption());
                mistake.setCorrectOptionText(getOptionLabel(q, q.getCorrectOption()));
                mistake.setExplanation(q.getExplanation());
                mistake.setMistakeCategory(q.getMistakeCategory().name());
                mistake.setMistakeCategoryLabel(q.getMistakeCategory().getLabel());
                mistake.setMistakeDescription(q.getMistakeCategory().getDescription());
                mistake.setTopic(q.getTopic());
                mistake.setCategory(q.getCategory().getDisplayName());
                mistake.setRevisionTopic(q.getRevisionTopic());
                mistake.setFollowUpTip(q.getFollowUpTip());
                mistakes.add(mistake);
            }
        }
        result.setMistakes(mistakes);

        // Trigger adaptive recommendations based on updated results
        result.setRecommendations(diagnosticService.generateAdaptiveRecommendations(user.getId()));

        return result;
    }

    public List<AssessmentAttempt> getAttemptsByUserId(Long userId) {
        return attemptRepository.findByUserIdOrderByStartedAtDesc(userId);
    }

    private String getOptionLabel(Question q, String option) {
        if (option == null) return "No answer submitted";
        return switch (option.toUpperCase()) {
            case "A" -> q.getOptionA();
            case "B" -> q.getOptionB();
            case "C" -> q.getOptionC();
            case "D" -> q.getOptionD();
            default -> "Option " + option;
        };
    }
}
