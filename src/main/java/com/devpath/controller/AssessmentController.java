package com.devpath.controller;

import com.devpath.dto.AssessmentResultDto;
import com.devpath.dto.AssessmentSubmissionDto;
import com.devpath.entity.Assessment;
import com.devpath.entity.AssessmentAttempt;
import com.devpath.entity.User;
import com.devpath.service.AssessmentService;
import com.devpath.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final AuthService authService;

    public AssessmentController(AssessmentService assessmentService, AuthService authService) {
        this.assessmentService = assessmentService;
        this.authService = authService;
    }

    @GetMapping("/assessments")
    public ResponseEntity<List<Assessment>> getAllAssessments() {
        return ResponseEntity.ok(assessmentService.getAllAssessments());
    }

    @GetMapping("/assessments/{id}")
    public ResponseEntity<Assessment> getAssessmentById(@PathVariable Long id) {
        return ResponseEntity.ok(assessmentService.getAssessmentById(id));
    }

    @PostMapping("/student/assessments/submit")
    public ResponseEntity<AssessmentResultDto> submitAssessment(@Valid @RequestBody AssessmentSubmissionDto submission) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(assessmentService.submitAssessment(user, submission));
    }

    @GetMapping("/student/assessments/history")
    public ResponseEntity<List<AssessmentAttempt>> getAttemptHistory() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(assessmentService.getAttemptsByUserId(user.getId()));
    }
}
