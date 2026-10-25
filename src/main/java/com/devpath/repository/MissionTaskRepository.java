package com.devpath.repository;

import com.devpath.entity.MissionTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MissionTaskRepository extends JpaRepository<MissionTask, Long> {
    List<MissionTask> findByMissionIdOrderByDayNumberAscOrderIndexAsc(Long missionId);
    long countByMissionId(Long missionId);
}
