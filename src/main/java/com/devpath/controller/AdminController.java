package com.devpath.controller;

import com.devpath.dto.AdminStatsDto;
import com.devpath.entity.*;
import com.devpath.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getStats() {
        return ResponseEntity.ok(adminService.getPlatformStats());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/lessons")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        return ResponseEntity.ok(adminService.saveLesson(lesson));
    }

    @PutMapping("/lessons/{id}")
    public ResponseEntity<Lesson> updateLesson(@PathVariable Long id, @RequestBody Lesson lesson) {
        lesson.setId(id);
        return ResponseEntity.ok(adminService.saveLesson(lesson));
    }

    @DeleteMapping("/lessons/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id) {
        adminService.deleteLesson(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/questions")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question) {
        return ResponseEntity.ok(adminService.saveQuestion(question));
    }

    @DeleteMapping("/questions/{id}")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        adminService.deleteQuestion(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/missions")
    public ResponseEntity<PlacementMission> createMission(@RequestBody PlacementMission mission) {
        return ResponseEntity.ok(adminService.saveMission(mission));
    }

    @GetMapping("/attempts")
    public ResponseEntity<List<AssessmentAttempt>> getAllAttempts() {
        return ResponseEntity.ok(adminService.getAllRecentAttempts());
    }
}
