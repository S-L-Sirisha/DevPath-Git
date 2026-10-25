package com.devpath.repository;

import com.devpath.entity.Assessment;
import com.devpath.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
    List<Assessment> findByCategory(SkillCategory category);
    List<Assessment> findByIsPlacementMockTrue();
}
