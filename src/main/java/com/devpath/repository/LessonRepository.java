package com.devpath.repository;

import com.devpath.entity.Lesson;
import com.devpath.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByLearningPathIdAndPublishedTrueOrderByOrderIndexAsc(Long pathId);
    Optional<Lesson> findBySlug(String slug);
    List<Lesson> findByCategoryAndPublishedTrue(SkillCategory category);
    List<Lesson> findByTopicContainingIgnoreCaseAndPublishedTrue(String topic);
    List<Lesson> findByPublishedTrue();
}
