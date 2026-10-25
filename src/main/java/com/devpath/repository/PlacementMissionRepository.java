package com.devpath.repository;

import com.devpath.entity.PlacementMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlacementMissionRepository extends JpaRepository<PlacementMission, Long> {
    List<PlacementMission> findAllByOrderByOrderIndexAsc();
}
