package com.devpath.dto;

import java.util.List;

public class StudentDashboardDto {
    private String studentName;
    private String targetRole;
    private String college;
    private RadarScoreDto radar;
    private ProofOfProgressDto progress;
    private List<AdaptiveRecommendationDto> recommendations;
    private List<AssessmentResultDto> recentAssessments;
    private Long completedLessonsCount;
    private Long solvedChallengesCount;
    private Long activeMissionsCount;

    public StudentDashboardDto() {
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public void setTargetRole(String targetRole) {
        this.targetRole = targetRole;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public RadarScoreDto getRadar() {
        return radar;
    }

    public void setRadar(RadarScoreDto radar) {
        this.radar = radar;
    }

    public ProofOfProgressDto getProgress() {
        return progress;
    }

    public void setProgress(ProofOfProgressDto progress) {
        this.progress = progress;
    }

    public List<AdaptiveRecommendationDto> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<AdaptiveRecommendationDto> recommendations) {
        this.recommendations = recommendations;
    }

    public List<AssessmentResultDto> getRecentAssessments() {
        return recentAssessments;
    }

    public void setRecentAssessments(List<AssessmentResultDto> recentAssessments) {
        this.recentAssessments = recentAssessments;
    }

    public Long getCompletedLessonsCount() {
        return completedLessonsCount;
    }

    public void setCompletedLessonsCount(Long completedLessonsCount) {
        this.completedLessonsCount = completedLessonsCount;
    }

    public Long getSolvedChallengesCount() {
        return solvedChallengesCount;
    }

    public void setSolvedChallengesCount(Long solvedChallengesCount) {
        this.solvedChallengesCount = solvedChallengesCount;
    }

    public Long getActiveMissionsCount() {
        return activeMissionsCount;
    }

    public void setActiveMissionsCount(Long activeMissionsCount) {
        this.activeMissionsCount = activeMissionsCount;
    }
}
