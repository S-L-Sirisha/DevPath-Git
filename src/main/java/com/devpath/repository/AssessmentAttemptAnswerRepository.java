package com.devpath.repository;

import com.devpath.entity.AssessmentAttemptAnswer;
import com.devpath.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssessmentAttemptAnswerRepository extends JpaRepository<AssessmentAttemptAnswer, Long> {
    
    @Query("SELECT a FROM AssessmentAttemptAnswer a WHERE a.attempt.user.id = :userId")
    List<AssessmentAttemptAnswer> findByUserId(@Param("userId") Long userId);

    @Query("SELECT a FROM AssessmentAttemptAnswer a WHERE a.attempt.user.id = :userId AND a.isCorrect = false")
    List<AssessmentAttemptAnswer> findMistakesByUserId(@Param("userId") Long userId);

    List<AssessmentAttemptAnswer> findByAttemptId(Long attemptId);

    @Query("SELECT a.category, COUNT(a), SUM(CASE WHEN a.isCorrect = true THEN 1 ELSE 0 END) " +
           "FROM AssessmentAttemptAnswer a WHERE a.attempt.user.id = :userId GROUP BY a.category")
    List<Object[]> getCategoryStatsByUserId(@Param("userId") Long userId);

    @Query("SELECT a.topic, COUNT(a), SUM(CASE WHEN a.isCorrect = true THEN 1 ELSE 0 END) " +
           "FROM AssessmentAttemptAnswer a WHERE a.attempt.user.id = :userId GROUP BY a.topic")
    List<Object[]> getTopicStatsByUserId(@Param("userId") Long userId);
}
