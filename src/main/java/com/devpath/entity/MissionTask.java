package com.devpath.entity;

import com.devpath.entity.enums.TaskType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "mission_tasks")
public class MissionTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private PlacementMission mission;

    @Column(name = "day_number", nullable = false)
    private Integer dayNumber = 1;

    @Column(name = "task_title", nullable = false, length = 150)
    private String taskTitle;

    @Column(name = "task_description", columnDefinition = "TEXT")
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type", nullable = false, length = 20)
    private TaskType taskType = TaskType.LESSON;

    @Column(name = "target_reference_id")
    private Long targetReferenceId;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    public MissionTask() {
    }

    public MissionTask(PlacementMission mission, Integer dayNumber, String taskTitle, String taskDescription, TaskType taskType, Long targetReferenceId, Integer orderIndex) {
        this.mission = mission;
        this.dayNumber = dayNumber;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskType = taskType;
        this.targetReferenceId = targetReferenceId;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlacementMission getMission() {
        return mission;
    }

    public void setMission(PlacementMission mission) {
        this.mission = mission;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Long getTargetReferenceId() {
        return targetReferenceId;
    }

    public void setTargetReferenceId(Long targetReferenceId) {
        this.targetReferenceId = targetReferenceId;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}
