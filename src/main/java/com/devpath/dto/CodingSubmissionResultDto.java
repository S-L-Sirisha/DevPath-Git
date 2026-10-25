package com.devpath.dto;

public class CodingSubmissionResultDto {
    private Long submissionId;
    private String status; // ACCEPTED, WRONG_ANSWER, RUNTIME_ERROR
    private Integer testCasesPassed;
    private Integer totalTestCases;
    private String message;
    private String timeComplexity;
    private String spaceComplexity;

    public CodingSubmissionResultDto() {
    }

    public CodingSubmissionResultDto(Long submissionId, String status, Integer testCasesPassed, Integer totalTestCases, String message, String timeComplexity, String spaceComplexity) {
        this.submissionId = submissionId;
        this.status = status;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
        this.message = message;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTestCasesPassed() {
        return testCasesPassed;
    }

    public void setTestCasesPassed(Integer testCasesPassed) {
        this.testCasesPassed = testCasesPassed;
    }

    public Integer getTotalTestCases() {
        return totalTestCases;
    }

    public void setTotalTestCases(Integer totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeComplexity() {
        return timeComplexity;
    }

    public void setTimeComplexity(String timeComplexity) {
        this.timeComplexity = timeComplexity;
    }

    public String getSpaceComplexity() {
        return spaceComplexity;
    }

    public void setSpaceComplexity(String spaceComplexity) {
        this.spaceComplexity = spaceComplexity;
    }
}
