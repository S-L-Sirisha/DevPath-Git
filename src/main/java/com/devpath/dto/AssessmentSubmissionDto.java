package com.devpath.dto;

import java.util.Map;

public class AssessmentSubmissionDto {
    private Long assessmentId;
    private Integer durationSeconds;
    // Map of questionId (as String or Long) -> selected option ("A", "B", "C", "D")
    private Map<Long, String> answers;

    public AssessmentSubmissionDto() {
    }

    public AssessmentSubmissionDto(Long assessmentId, Integer durationSeconds, Map<Long, String> answers) {
        this.assessmentId = assessmentId;
        this.durationSeconds = durationSeconds;
        this.answers = answers;
    }

    public Long getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(Long assessmentId) {
        this.assessmentId = assessmentId;
    }

    public Integer getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(Integer durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public Map<Long, String> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String> answers) {
        this.answers = answers;
    }
}
