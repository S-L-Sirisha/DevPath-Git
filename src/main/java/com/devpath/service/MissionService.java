package com.devpath.service;

import com.devpath.entity.MissionTask;
import com.devpath.entity.PlacementMission;
import com.devpath.entity.StudentMissionProgress;
import com.devpath.entity.User;
import com.devpath.entity.enums.MissionStatus;
import com.devpath.exception.ResourceNotFoundException;
import com.devpath.repository.MissionTaskRepository;
import com.devpath.repository.PlacementMissionRepository;
import com.devpath.repository.StudentMissionProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MissionService {

    private final PlacementMissionRepository missionRepository;
    private final MissionTaskRepository taskRepository;
    private final StudentMissionProgressRepository progressRepository;

    public MissionService(PlacementMissionRepository missionRepository,
                          MissionTaskRepository taskRepository,
                          StudentMissionProgressRepository progressRepository) {
        this.missionRepository = missionRepository;
        this.taskRepository = taskRepository;
        this.progressRepository = progressRepository;
    }

    public List<PlacementMission> getAllMissions() {
        return missionRepository.findAllByOrderByOrderIndexAsc();
    }

    public PlacementMission getMissionById(Long id) {
        return missionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mission not found with id: " + id));
    }

    public List<StudentMissionProgress> getStudentMissions(Long userId) {
        return progressRepository.findByUserId(userId);
    }

    @Transactional
    public StudentMissionProgress startMission(User user, Long missionId) {
        PlacementMission mission = getMissionById(missionId);
        Optional<StudentMissionProgress> existing = progressRepository.findByUserIdAndMissionId(user.getId(), missionId);

        if (existing.isPresent()) {
            return existing.get();
        }

        int totalTasks = mission.getTasks() != null ? mission.getTasks().size() : (int) taskRepository.countByMissionId(missionId);
        StudentMissionProgress progress = new StudentMissionProgress(user, mission, totalTasks);
        return progressRepository.save(progress);
    }

    @Transactional
    public StudentMissionProgress completeTask(User user, Long missionId, Long taskId) {
        PlacementMission mission = getMissionById(missionId);
        StudentMissionProgress progress = progressRepository.findByUserIdAndMissionId(user.getId(), missionId)
                .orElseGet(() -> startMission(user, missionId));

        progress.setCompletedTasksCount(Math.min(progress.getTotalTasksCount(), progress.getCompletedTasksCount() + 1));
        progress.setLastActivityAt(LocalDateTime.now());

        if (progress.getCompletedTasksCount() >= progress.getTotalTasksCount()) {
            progress.setStatus(MissionStatus.COMPLETED);
            progress.setCompletedAt(LocalDateTime.now());
        } else {
            progress.setStatus(MissionStatus.IN_PROGRESS);
            // Increment day if applicable
            int currentDay = Math.min(mission.getDurationDays(), (progress.getCompletedTasksCount() / 2) + 1);
            progress.setCurrentDay(currentDay);
        }

        return progressRepository.save(progress);
    }
}
