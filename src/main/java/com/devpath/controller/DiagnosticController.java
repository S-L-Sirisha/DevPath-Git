package com.devpath.controller;

import com.devpath.dto.*;
import com.devpath.entity.AssessmentAttempt;
import com.devpath.entity.User;
import com.devpath.service.AssessmentService;
import com.devpath.service.AuthService;
import com.devpath.service.DiagnosticService;
import com.devpath.service.LearningService;
import com.devpath.service.MissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/student")
public class DiagnosticController {

    private final DiagnosticService diagnosticService;
    private final AuthService authService;
    private final AssessmentService assessmentService;
    private final LearningService learningService;
    private final MissionService missionService;

    public DiagnosticController(DiagnosticService diagnosticService,
                                AuthService authService,
                                AssessmentService assessmentService,
                                LearningService learningService,
                                MissionService missionService) {
        this.diagnosticService = diagnosticService;
        this.authService = authService;
        this.assessmentService = assessmentService;
        this.learningService = learningService;
        this.missionService = missionService;
    }

    @GetMapping("/radar")
    public ResponseEntity<RadarScoreDto> getRadarScores() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(diagnosticService.calculateRadarScores(user.getId()));
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<AdaptiveRecommendationDto>> getRecommendations() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(diagnosticService.generateAdaptiveRecommendations(user.getId()));
    }

    @GetMapping("/mistakes")
    public ResponseEntity<List<MistakeDetailDto>> getMistakes() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(diagnosticService.getMistakesByStudent(user.getId()));
    }

    @GetMapping("/progress")
    public ResponseEntity<ProofOfProgressDto> getProofOfProgress() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(diagnosticService.calculateProofOfProgress(user.getId()));
    }

    @GetMapping("/dashboard")
    public ResponseEntity<StudentDashboardDto> getDashboard() {
        User user = authService.getCurrentAuthenticatedUser();

        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setStudentName(user.getName());
        dto.setTargetRole(user.getTargetRole());
        dto.setCollege(user.getCollege());

        dto.setRadar(diagnosticService.calculateRadarScores(user.getId()));
        dto.setProgress(diagnosticService.calculateProofOfProgress(user.getId()));
        dto.setRecommendations(diagnosticService.generateAdaptiveRecommendations(user.getId()));

        List<AssessmentAttempt> attempts = assessmentService.getAttemptsByUserId(user.getId());
        List<AssessmentResultDto> recent = new ArrayList<>();
        for (int i = 0; i < Math.min(3, attempts.size()); i++) {
            AssessmentAttempt a = attempts.get(i);
            AssessmentResultDto res = new AssessmentResultDto();
            res.setAttemptId(a.getId());
            res.setAssessmentId(a.getAssessment().getId());
            res.setAssessmentTitle(a.getAssessment().getTitle());
            res.setScore(a.getScore());
            res.setTotalMarks(a.getTotalMarks());
            res.setPercentage(a.getPercentage());
            res.setPassed(a.getPassed());
            res.setDurationSeconds(a.getDurationSeconds());
            res.setCompletedAt(a.getCompletedAt());
            recent.add(res);
        }
        dto.setRecentAssessments(recent);

        dto.setCompletedLessonsCount(learningService.getCompletedLessonCount(user.getId()));
        dto.setSolvedChallengesCount(dto.getProgress().getTotalChallengesSolved());
        dto.setActiveMissionsCount((long) missionService.getStudentMissions(user.getId()).size());

        return ResponseEntity.ok(dto);
    }
}
