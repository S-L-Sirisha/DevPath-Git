package com.devpath.controller;

import com.devpath.dto.CodingSubmissionDto;
import com.devpath.dto.CodingSubmissionResultDto;
import com.devpath.entity.CodingChallenge;
import com.devpath.entity.CodingSubmission;
import com.devpath.entity.User;
import com.devpath.entity.enums.SkillCategory;
import com.devpath.service.AuthService;
import com.devpath.service.CodingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CodingController {

    private final CodingService codingService;
    private final AuthService authService;

    public CodingController(CodingService codingService, AuthService authService) {
        this.codingService = codingService;
        this.authService = authService;
    }

    @GetMapping("/challenges")
    public ResponseEntity<List<CodingChallenge>> getAllChallenges(@RequestParam(required = false) SkillCategory category) {
        if (category != null) {
            return ResponseEntity.ok(codingService.getChallengesByCategory(category));
        }
        return ResponseEntity.ok(codingService.getAllChallenges());
    }

    @GetMapping("/challenges/{id}")
    public ResponseEntity<CodingChallenge> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(codingService.getChallengeById(id));
    }

    @GetMapping("/challenges/slug/{slug}")
    public ResponseEntity<CodingChallenge> getChallengeBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(codingService.getChallengeBySlug(slug));
    }

    @PostMapping("/student/challenges/submit")
    public ResponseEntity<CodingSubmissionResultDto> submitSolution(@Valid @RequestBody CodingSubmissionDto dto) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(codingService.submitSolution(user, dto));
    }

    @GetMapping("/student/challenges/submissions")
    public ResponseEntity<List<CodingSubmission>> getUserSubmissions(@RequestParam(required = false) Long challengeId) {
        User user = authService.getCurrentAuthenticatedUser();
        if (challengeId != null) {
            return ResponseEntity.ok(codingService.getUserSubmissionsForChallenge(user.getId(), challengeId));
        }
        return ResponseEntity.ok(codingService.getUserSubmissions(user.getId()));
    }
}
