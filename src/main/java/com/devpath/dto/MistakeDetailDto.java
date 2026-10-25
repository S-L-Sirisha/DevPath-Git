package com.devpath.dto;

public class MistakeDetailDto {
    private Long questionId;
    private String questionText;
    private String codeSnippet;
    private String selectedOption;
    private String selectedOptionText;
    private String correctOption;
    private String correctOptionText;
    private String explanation;
    private String mistakeCategory;
    private String mistakeCategoryLabel;
    private String mistakeDescription;
    private String topic;
    private String category;
    private String revisionTopic;
    private String followUpTip;

    public MistakeDetailDto() {
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCodeSnippet() {
        return codeSnippet;
    }

    public void setCodeSnippet(String codeSnippet) {
        this.codeSnippet = codeSnippet;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getSelectedOptionText() {
        return selectedOptionText;
    }

    public void setSelectedOptionText(String selectedOptionText) {
        this.selectedOptionText = selectedOptionText;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getCorrectOptionText() {
        return correctOptionText;
    }

    public void setCorrectOptionText(String correctOptionText) {
        this.correctOptionText = correctOptionText;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getMistakeCategory() {
        return mistakeCategory;
    }

    public void setMistakeCategory(String mistakeCategory) {
        this.mistakeCategory = mistakeCategory;
    }

    public String getMistakeCategoryLabel() {
        return mistakeCategoryLabel;
    }

    public void setMistakeCategoryLabel(String mistakeCategoryLabel) {
        this.mistakeCategoryLabel = mistakeCategoryLabel;
    }

    public String getMistakeDescription() {
        return mistakeDescription;
    }

    public void setMistakeDescription(String mistakeDescription) {
        this.mistakeDescription = mistakeDescription;
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

    public String getRevisionTopic() {
        return revisionTopic;
    }

    public void setRevisionTopic(String revisionTopic) {
        this.revisionTopic = revisionTopic;
    }

    public String getFollowUpTip() {
        return followUpTip;
    }

    public void setFollowUpTip(String followUpTip) {
        this.followUpTip = followUpTip;
    }
}
