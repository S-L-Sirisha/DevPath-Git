package com.devpath.repository;

import com.devpath.entity.CodingSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodingSubmissionRepository extends JpaRepository<CodingSubmission, Long> {
    List<CodingSubmission> findByUserIdOrderBySubmittedAtDesc(Long userId);
    List<CodingSubmission> findByUserIdAndChallengeIdOrderBySubmittedAtDesc(Long userId, Long challengeId);
    long countByUserIdAndStatus(Long userId, String status);
    long countByUserId(Long userId);
}
