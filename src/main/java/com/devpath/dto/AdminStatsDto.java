package com.devpath.dto;

public class AdminStatsDto {
    private Long totalStudents = 0L;
    private Long totalLessons = 0L;
    private Long totalQuestions = 0L;
    private Long totalAssessmentsTaken = 0L;
    private Long totalCodeSubmissions = 0L;
    private Double avgReadinessScore = 0.0;
    private Long totalMissions = 0L;

    public AdminStatsDto() {
    }

    public Long getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(Long totalStudents) {
        this.totalStudents = totalStudents;
    }

    public Long getTotalLessons() {
        return totalLessons;
    }

    public void setTotalLessons(Long totalLessons) {
        this.totalLessons = totalLessons;
    }

    public Long getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Long totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Long getTotalAssessmentsTaken() {
        return totalAssessmentsTaken;
    }

    public void setTotalAssessmentsTaken(Long totalAssessmentsTaken) {
        this.totalAssessmentsTaken = totalAssessmentsTaken;
    }

    public Long getTotalCodeSubmissions() {
        return totalCodeSubmissions;
    }

    public void setTotalCodeSubmissions(Long totalCodeSubmissions) {
        this.totalCodeSubmissions = totalCodeSubmissions;
    }

    public Double getAvgReadinessScore() {
        return avgReadinessScore;
    }

    public void setAvgReadinessScore(Double avgReadinessScore) {
        this.avgReadinessScore = avgReadinessScore;
    }

    public Long getTotalMissions() {
        return totalMissions;
    }

    public void setTotalMissions(Long totalMissions) {
        this.totalMissions = totalMissions;
    }
}
