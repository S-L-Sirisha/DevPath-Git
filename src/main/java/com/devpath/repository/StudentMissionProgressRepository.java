package com.devpath.repository;

import com.devpath.entity.StudentMissionProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentMissionProgressRepository extends JpaRepository<StudentMissionProgress, Long> {
    List<StudentMissionProgress> findByUserId(Long userId);
    Optional<StudentMissionProgress> findByUserIdAndMissionId(Long userId, Long missionId);
}
