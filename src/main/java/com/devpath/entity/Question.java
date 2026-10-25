package com.devpath.entity;

import com.devpath.entity.enums.Difficulty;
import com.devpath.entity.enums.MistakeCategory;
import com.devpath.entity.enums.SkillCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SkillCategory category;

    @Column(nullable = false, length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.EASY;

    @Column(name = "question_text", nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(name = "code_snippet", columnDefinition = "TEXT")
    private String codeSnippet;

    @Column(name = "option_a", nullable = false, columnDefinition = "TEXT")
    private String optionA;

    @Column(name = "option_b", nullable = false, columnDefinition = "TEXT")
    private String optionB;

    @Column(name = "option_c", nullable = false, columnDefinition = "TEXT")
    private String optionC;

    @Column(name = "option_d", nullable = false, columnDefinition = "TEXT")
    private String optionD;

    @Column(name = "correct_option", nullable = false, length = 5)
    private String correctOption; // "A", "B", "C", "D"

    @Column(columnDefinition = "TEXT")
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "mistake_category", nullable = false, length = 50)
    private MistakeCategory mistakeCategory = MistakeCategory.CONCEPT_MISUNDERSTANDING;

    @Column(name = "revision_topic", length = 150)
    private String revisionTopic;

    @Column(name = "follow_up_tip", columnDefinition = "TEXT")
    private String followUpTip;

    public Question() {
    }

    public Question(SkillCategory category, String topic, Difficulty difficulty, String questionText, String codeSnippet, String optionA, String optionB, String optionC, String optionD, String correctOption, String explanation, MistakeCategory mistakeCategory, String revisionTopic, String followUpTip) {
        this.category = category;
        this.topic = topic;
        this.difficulty = difficulty;
        this.questionText = questionText;
        this.codeSnippet = codeSnippet;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctOption = correctOption;
        this.explanation = explanation;
        this.mistakeCategory = mistakeCategory;
        this.revisionTopic = revisionTopic;
        this.followUpTip = followUpTip;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SkillCategory getCategory() {
        return category;
    }

    public void setCategory(SkillCategory category) {
        this.category = category;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
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

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public MistakeCategory getMistakeCategory() {
        return mistakeCategory;
    }

    public void setMistakeCategory(MistakeCategory mistakeCategory) {
        this.mistakeCategory = mistakeCategory;
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
