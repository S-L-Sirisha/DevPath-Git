package com.devpath.service;

import com.devpath.dto.AdminStatsDto;
import com.devpath.entity.*;
import com.devpath.exception.ResourceNotFoundException;
import com.devpath.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final LessonRepository lessonRepository;
    private final QuestionRepository questionRepository;
    private final AssessmentRepository assessmentRepository;
    private final AssessmentAttemptRepository attemptRepository;
    private final CodingSubmissionRepository submissionRepository;
    private final PlacementMissionRepository missionRepository;

    public AdminService(UserRepository userRepository,
                        LessonRepository lessonRepository,
                        QuestionRepository questionRepository,
                        AssessmentRepository assessmentRepository,
                        AssessmentAttemptRepository attemptRepository,
                        CodingSubmissionRepository submissionRepository,
                        PlacementMissionRepository missionRepository) {
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.questionRepository = questionRepository;
        this.assessmentRepository = assessmentRepository;
        this.attemptRepository = attemptRepository;
        this.submissionRepository = submissionRepository;
        this.missionRepository = missionRepository;
    }

    @Transactional(readOnly = true)
    public AdminStatsDto getPlatformStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalStudents(userRepository.count());
        stats.setTotalLessons(lessonRepository.count());
        stats.setTotalQuestions(questionRepository.count());
        stats.setTotalAssessmentsTaken(attemptRepository.count());
        stats.setTotalCodeSubmissions(submissionRepository.count());
        stats.setTotalMissions(missionRepository.count());

        List<AssessmentAttempt> attempts = attemptRepository.findAll();
        double avg = attempts.stream().mapToDouble(AssessmentAttempt::getPercentage).average().orElse(0.0);
        stats.setAvgReadinessScore(Math.round(avg * 10.0) / 10.0);

        return stats;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public Lesson saveLesson(Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }

    @Transactional
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!questionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Question not found with id: " + id);
        }
        questionRepository.deleteById(id);
    }

    @Transactional
    public PlacementMission saveMission(PlacementMission mission) {
        return missionRepository.save(mission);
    }

    public List<AssessmentAttempt> getAllRecentAttempts() {
        return attemptRepository.findAll();
    }
}
