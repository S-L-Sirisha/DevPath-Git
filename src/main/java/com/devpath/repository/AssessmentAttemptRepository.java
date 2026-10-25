package com.devpath.repository;

import com.devpath.entity.AssessmentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {
    List<AssessmentAttempt> findByUserIdOrderByStartedAtDesc(Long userId);
    Optional<AssessmentAttempt> findFirstByUserIdOrderByStartedAtAsc(Long userId);
    Optional<AssessmentAttempt> findFirstByUserIdOrderByCompletedAtDesc(Long userId);
    long countByUserId(Long userId);
}
