package com.devpath.repository;

import com.devpath.entity.CodingChallenge;
import com.devpath.entity.enums.SkillCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodingChallengeRepository extends JpaRepository<CodingChallenge, Long> {
    Optional<CodingChallenge> findBySlug(String slug);
    List<CodingChallenge> findByCategory(SkillCategory category);
}
