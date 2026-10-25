package com.devpath.controller;

import com.devpath.entity.LearningPath;
import com.devpath.entity.Lesson;
import com.devpath.entity.StudentLessonProgress;
import com.devpath.entity.User;
import com.devpath.entity.enums.SkillCategory;
import com.devpath.service.AuthService;
import com.devpath.service.LearningService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class LearningController {

    private final LearningService learningService;
    private final AuthService authService;

    public LearningController(LearningService learningService, AuthService authService) {
        this.learningService = learningService;
        this.authService = authService;
    }

    @GetMapping("/learning-paths")
    public ResponseEntity<List<LearningPath>> getAllPaths() {
        return ResponseEntity.ok(learningService.getAllLearningPaths());
    }

    @GetMapping("/learning-paths/{slug}")
    public ResponseEntity<LearningPath> getPathBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(learningService.getLearningPathBySlug(slug));
    }

    @GetMapping("/learning-paths/{id}/lessons")
    public ResponseEntity<List<Lesson>> getLessonsByPath(@PathVariable Long id) {
        return ResponseEntity.ok(learningService.getLessonsByPathId(id));
    }

    @GetMapping("/lessons/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable Long id) {
        return ResponseEntity.ok(learningService.getLessonById(id));
    }

    @GetMapping("/lessons/slug/{slug}")
    public ResponseEntity<Lesson> getLessonBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(learningService.getLessonBySlug(slug));
    }

    @GetMapping("/lessons/category/{category}")
    public ResponseEntity<List<Lesson>> getLessonsByCategory(@PathVariable SkillCategory category) {
        return ResponseEntity.ok(learningService.getLessonsByCategory(category));
    }

    @PostMapping("/student/lessons/{id}/toggle-complete")
    public ResponseEntity<StudentLessonProgress> toggleLesson(@PathVariable Long id) {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(learningService.toggleLessonCompletion(user, id));
    }

    @GetMapping("/student/completed-lessons")
    public ResponseEntity<Set<Long>> getCompletedLessonIds() {
        User user = authService.getCurrentAuthenticatedUser();
        return ResponseEntity.ok(learningService.getCompletedLessonIds(user.getId()));
    }
}
