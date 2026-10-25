package com.devpath.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProofOfProgressDto {
    private Double initialScorePercentage = 0.0;
    private Double currentScorePercentage = 0.0;
    private Double percentagePointsDelta = 0.0;
    private Long totalAssessmentsTaken = 0L;
    private Long totalLessonsCompleted = 0L;
    private Long totalChallengesSolved = 0L;
    private LocalDateTime baselineAssessmentDate;
    private LocalDateTime latestAssessmentDate;
    private List<String> initialWeakAreas;
    private List<String> currentStrengths;
    private Boolean isDemoData = false;

    public ProofOfProgressDto() {
    }

    public Double getInitialScorePercentage() {
        return initialScorePercentage;
    }

    public void setInitialScorePercentage(Double initialScorePercentage) {
        this.initialScorePercentage = initialScorePercentage;
    }

    public Double getCurrentScorePercentage() {
        return currentScorePercentage;
    }

    public void setCurrentScorePercentage(Double currentScorePercentage) {
        this.currentScorePercentage = currentScorePercentage;
    }

    public Double getPercentagePointsDelta() {
        return percentagePointsDelta;
    }

    public void setPercentagePointsDelta(Double percentagePointsDelta) {
        this.percentagePointsDelta = percentagePointsDelta;
    }

    public Long getTotalAssessmentsTaken() {
        return totalAssessmentsTaken;
    }

    public void setTotalAssessmentsTaken(Long totalAssessmentsTaken) {
        this.totalAssessmentsTaken = totalAssessmentsTaken;
    }

    public Long getTotalLessonsCompleted() {
        return totalLessonsCompleted;
    }

    public void setTotalLessonsCompleted(Long totalLessonsCompleted) {
        this.totalLessonsCompleted = totalLessonsCompleted;
    }

    public Long getTotalChallengesSolved() {
        return totalChallengesSolved;
    }

    public void setTotalChallengesSolved(Long totalChallengesSolved) {
        this.totalChallengesSolved = totalChallengesSolved;
    }

    public LocalDateTime getBaselineAssessmentDate() {
        return baselineAssessmentDate;
    }

    public void setBaselineAssessmentDate(LocalDateTime baselineAssessmentDate) {
        this.baselineAssessmentDate = baselineAssessmentDate;
    }

    public LocalDateTime getLatestAssessmentDate() {
        return latestAssessmentDate;
    }

    public void setLatestAssessmentDate(LocalDateTime latestAssessmentDate) {
        this.latestAssessmentDate = latestAssessmentDate;
    }

    public List<String> getInitialWeakAreas() {
        return initialWeakAreas;
    }

    public void setInitialWeakAreas(List<String> initialWeakAreas) {
        this.initialWeakAreas = initialWeakAreas;
    }

    public List<String> getCurrentStrengths() {
        return currentStrengths;
    }

    public void setCurrentStrengths(List<String> currentStrengths) {
        this.currentStrengths = currentStrengths;
    }

    public Boolean getIsDemoData() {
        return isDemoData;
    }

    public void setIsDemoData(Boolean demoData) {
        isDemoData = demoData;
    }
}
