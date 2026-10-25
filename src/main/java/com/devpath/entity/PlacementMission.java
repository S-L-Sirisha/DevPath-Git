package com.devpath.entity;

import com.devpath.entity.enums.SkillCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "placement_missions")
public class PlacementMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays = 7;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SkillCategory category;

    @Column(length = 50)
    private String icon = "trophy";

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC, orderIndex ASC")
    private List<MissionTask> tasks = new ArrayList<>();

    public PlacementMission() {
    }

    public PlacementMission(String title, String description, Integer durationDays, SkillCategory category, String icon, Integer orderIndex) {
        this.title = title;
        this.description = description;
        this.durationDays = durationDays;
        this.category = category;
        this.icon = icon;
        this.orderIndex = orderIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public List<MissionTask> getTasks() {
        return tasks;
    }

    public void setTasks(List<MissionTask> tasks) {
        this.tasks = tasks;
    }
}
