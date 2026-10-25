package com.devpath.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CodingSubmissionDto {
    @NotNull
    private Long challengeId;

    @NotBlank
    private String language = "java";

    @NotBlank
    private String codeContent;

    public CodingSubmissionDto() {
    }

    public CodingSubmissionDto(Long challengeId, String language, String codeContent) {
        this.challengeId = challengeId;
        this.language = language;
        this.codeContent = codeContent;
    }

    public Long getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(Long challengeId) {
        this.challengeId = challengeId;
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
}
