package com.devpath.service;

import com.devpath.entity.LearningPath;
import com.devpath.entity.Lesson;
import com.devpath.entity.StudentLessonProgress;
import com.devpath.entity.User;
import com.devpath.entity.enums.SkillCategory;
import com.devpath.exception.ResourceNotFoundException;
import com.devpath.repository.LearningPathRepository;
import com.devpath.repository.LessonRepository;
import com.devpath.repository.StudentLessonProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LearningService {

    private final LearningPathRepository learningPathRepository;
    private final LessonRepository lessonRepository;
    private final StudentLessonProgressRepository progressRepository;

    public LearningService(LearningPathRepository learningPathRepository,
                           LessonRepository lessonRepository,
                           StudentLessonProgressRepository progressRepository) {
        this.learningPathRepository = learningPathRepository;
        this.lessonRepository = lessonRepository;
        this.progressRepository = progressRepository;
    }

    public List<LearningPath> getAllLearningPaths() {
        return learningPathRepository.findByPublishedTrueOrderByOrderIndexAsc();
    }

    public LearningPath getLearningPathBySlug(String slug) {
        return learningPathRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Learning path not found: " + slug));
    }

    public List<Lesson> getLessonsByPathId(Long pathId) {
        return lessonRepository.findByLearningPathIdAndPublishedTrueOrderByOrderIndexAsc(pathId);
    }

    public Lesson getLessonById(Long id) {
        return lessonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with id: " + id));
    }

    public Lesson getLessonBySlug(String slug) {
        return lessonRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson not found with slug: " + slug));
    }

    public List<Lesson> getLessonsByCategory(SkillCategory category) {
        return lessonRepository.findByCategoryAndPublishedTrue(category);
    }

    @Transactional
    public StudentLessonProgress toggleLessonCompletion(User user, Long lessonId) {
        Lesson lesson = getLessonById(lessonId);
        Optional<StudentLessonProgress> existing = progressRepository.findByUserIdAndLessonId(user.getId(), lessonId);

        StudentLessonProgress progress;
        if (existing.isPresent()) {
            progress = existing.get();
            boolean newStatus = !progress.getCompleted();
            progress.setCompleted(newStatus);
            progress.setCompletedAt(newStatus ? LocalDateTime.now() : null);
        } else {
            progress = new StudentLessonProgress(user, lesson, true);
        }

        return progressRepository.save(progress);
    }

    public Set<Long> getCompletedLessonIds(Long userId) {
        return progressRepository.findByUserId(userId).stream()
                .filter(StudentLessonProgress::getCompleted)
                .map(p -> p.getLesson().getId())
                .collect(Collectors.toSet());
    }

    public long getCompletedLessonCount(Long userId) {
        return progressRepository.countByUserIdAndCompletedTrue(userId);
    }
}
