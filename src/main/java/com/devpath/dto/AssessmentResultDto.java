package com.devpath.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class AssessmentResultDto {
    private Long attemptId;
    private Long assessmentId;
    private String assessmentTitle;
    private Integer score;
    private Integer totalMarks;
    private Double percentage;
    private Boolean passed;
    private Integer durationSeconds;
    private LocalDateTime completedAt;
    private Map<String, Double> categoryBreakdown;
    private List<MistakeDetailDto> mistakes;
    private List<AdaptiveRecommendationDto> recommendations;

    public AssessmentResultDto() {
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public String getAssessmentTitle() {
        return assessmentTitle;
    }

    public void setAssessmentTitle(String assessmentTitle) {
        this.assessmentTitle = assessmentTitle;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(Integer totalMarks) {
        this.totalMarks = totalMarks;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Boolean getPassed() {
        return passed;
    }

    public void setPassed(Boolean passed) {
        this.passed = passed;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public Map<String, Double> getCategoryBreakdown() {
        return categoryBreakdown;
    }

    public void setCategoryBreakdown(Map<String, Double> categoryBreakdown) {
        this.categoryBreakdown = categoryBreakdown;
    }

    public List<MistakeDetailDto> getMistakes() {
        return mistakes;
    }

    public void setMistakes(List<MistakeDetailDto> mistakes) {
        this.mistakes = mistakes;
    }

    public List<AdaptiveRecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<AdaptiveRecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }
}
