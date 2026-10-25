package com.devpath.entity;

import com.devpath.entity.enums.MissionStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_mission_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "mission_id"})
})
public class StudentMissionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "mission_id", nullable = false)
    private PlacementMission mission;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private MissionStatus status = MissionStatus.NOT_STARTED;

    @Column(name = "current_day", nullable = false)
    private Integer currentDay = 1;

    @Column(name = "completed_tasks_count", nullable = false)
    private Integer completedTasksCount = 0;

    @Column(name = "total_tasks_count", nullable = false)
    private Integer totalTasksCount = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public StudentMissionProgress() {
    }

    public StudentMissionProgress(User user, PlacementMission mission, Integer totalTasksCount) {
        this.user = user;
        this.mission = mission;
        this.totalTasksCount = totalTasksCount;
        this.status = MissionStatus.IN_PROGRESS;
        this.currentDay = 1;
        this.completedTasksCount = 0;
        this.startedAt = LocalDateTime.now();
        this.lastActivityAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlacementMission getMission() {
        return mission;
    }

    public void setMission(PlacementMission mission) {
        this.mission = mission;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public void setStatus(MissionStatus status) {
        this.status = status;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    public Integer getCompletedTasksCount() {
        return completedTasksCount;
    }

    public void setCompletedTasksCount(Integer completedTasksCount) {
        this.completedTasksCount = completedTasksCount;
    }

    public Integer getTotalTasksCount() {
        return totalTasksCount;
    }

    public void setTotalTasksCount(Integer totalTasksCount) {
        this.totalTasksCount = totalTasksCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
}
