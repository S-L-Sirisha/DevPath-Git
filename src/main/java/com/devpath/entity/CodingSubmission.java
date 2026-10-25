package com.devpath.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "coding_submissions")
public class CodingSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "challenge_id", nullable = false)
    private CodingChallenge challenge;

    @Column(nullable = false, length = 30)
    private String language = "java";

    @Lob
    @Column(name = "code_content", nullable = false, columnDefinition = "LONGTEXT")
    private String codeContent;

    @Column(nullable = false, length = 30)
    private String status = "ACCEPTED"; // ACCEPTED, WRONG_ANSWER, RUNTIME_ERROR

    @Column(name = "test_cases_passed", nullable = false)
    private Integer testCasesPassed = 0;

    @Column(name = "total_test_cases", nullable = false)
    private Integer totalTestCases = 0;

    @Column(name = "submitted_at", nullable = false)
    private LocalDateTime submittedAt;

    public CodingSubmission() {
    }

    public CodingSubmission(User user, CodingChallenge challenge, String language, String codeContent, String status, Integer testCasesPassed, Integer totalTestCases) {
        this.user = user;
        this.challenge = challenge;
        this.language = language;
        this.codeContent = codeContent;
        this.status = status;
        this.testCasesPassed = testCasesPassed;
        this.totalTestCases = totalTestCases;
        this.submittedAt = LocalDateTime.now();
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

    public CodingChallenge getChallenge() {
        return challenge;
    }

    public void setChallenge(CodingChallenge challenge) {
        this.challenge = challenge;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCodeContent() {
        return codeContent;
    }

    public void setCodeContent(String codeContent) {
        this.codeContent = codeContent;
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

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}
