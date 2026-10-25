package com.devpath.entity;

import com.devpath.entity.enums.MistakeCategory;
import com.devpath.entity.enums.SkillCategory;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "assessment_attempt_answers")
public class AssessmentAttemptAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private AssessmentAttempt attempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "selected_option", length = 5)
    private String selectedOption; // "A", "B", "C", "D"

    @Column(name = "correct_option", nullable = false, length = 5)
    private String correctOption;

    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "mistake_category", length = 50)
    private MistakeCategory mistakeCategory;

    @Column(length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private SkillCategory category;

    public AssessmentAttemptAnswer() {
    }

    public AssessmentAttemptAnswer(AssessmentAttempt attempt, Question question, String selectedOption, String correctOption, Boolean isCorrect, MistakeCategory mistakeCategory, String topic, SkillCategory category) {
        this.attempt = attempt;
        this.question = question;
        this.selectedOption = selectedOption;
        this.correctOption = correctOption;
        this.isCorrect = isCorrect;
        this.mistakeCategory = mistakeCategory;
        this.topic = topic;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AssessmentAttempt getAttempt() {
        return attempt;
    }

    public void setAttempt(AssessmentAttempt attempt) {
        this.attempt = attempt;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(String selectedOption) {
        this.selectedOption = selectedOption;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public MistakeCategory getMistakeCategory() {
        return mistakeCategory;
    }

    public void setMistakeCategory(MistakeCategory mistakeCategory) {
        this.mistakeCategory = mistakeCategory;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }
}
