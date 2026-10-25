package com.devpath.entity;

import com.devpath.entity.enums.Difficulty;
import com.devpath.entity.enums.SkillCategory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessments")
public class Assessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SkillCategory category; // null if mixed/comprehensive mock

    @Column(name = "time_limit_minutes", nullable = false)
    private Integer timeLimitMinutes = 15;

    @Column(name = "passing_score_percentage")
    private Integer passingScorePercentage = 60;

    @Column(name = "total_questions")
    private Integer totalQuestions = 10;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Difficulty difficulty = Difficulty.MEDIUM;

    @Column(name = "is_placement_mock", nullable = false)
    private Boolean isPlacementMock = false;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "assessment_questions",
        joinColumns = @JoinColumn(name = "assessment_id"),
        inverseJoinColumns = @JoinColumn(name = "question_id")
    )
    private List<Question> questions = new ArrayList<>();

    public Assessment() {
    }

    public Assessment(String title, String description, SkillCategory category, Integer timeLimitMinutes, Integer passingScorePercentage, Integer totalQuestions, Difficulty difficulty, Boolean isPlacementMock) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.timeLimitMinutes = timeLimitMinutes;
        this.passingScorePercentage = passingScorePercentage;
        this.totalQuestions = totalQuestions;
        this.difficulty = difficulty;
        this.isPlacementMock = isPlacementMock;
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

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public Integer getTimeLimitMinutes() {
        return timeLimitMinutes;
    }

    public void setTimeLimitMinutes(Integer timeLimitMinutes) {
        this.timeLimitMinutes = timeLimitMinutes;
    }

    public Integer getPassingScorePercentage() {
        return passingScorePercentage;
    }

    public void setPassingScorePercentage(Integer passingScorePercentage) {
        this.passingScorePercentage = passingScorePercentage;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public Boolean getIsPlacementMock() {
        return isPlacementMock;
    }

    public void setIsPlacementMock(Boolean placementMock) {
        isPlacementMock = placementMock;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
