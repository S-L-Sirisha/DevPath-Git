package com.devpath.dto;

public class AdaptiveRecommendationDto {
    private String type; // "LESSON", "PRACTICE_QUESTION", "CODING_CHALLENGE"
    private String title;
    private String topic;
    private String category;
    private Long referenceId;
    private String referenceSlug;
    private String reason; // Rule-based transparent diagnostic reasoning
    private String priority; // "HIGH", "MEDIUM", "LOW"

    public AdaptiveRecommendationDto() {
    }

    public AdaptiveRecommendationDto(String type, String title, String topic, String category, Long referenceId, String referenceSlug, String reason, String priority) {
        this.type = type;
        this.title = title;
        this.topic = topic;
        this.category = category;
        this.referenceId = referenceId;
        this.referenceSlug = referenceSlug;
        this.reason = reason;
        this.priority = priority;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceSlug() {
        return referenceSlug;
    }

    public void setReferenceSlug(String referenceSlug) {
        this.referenceSlug = referenceSlug;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
