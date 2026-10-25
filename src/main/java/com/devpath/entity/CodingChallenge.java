package com.devpath.entity;

import com.devpath.entity.enums.Difficulty;
import com.devpath.entity.enums.SkillCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "coding_challenges")
public class CodingChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, unique = true, length = 150)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SkillCategory category;

    @Column(nullable = false, length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.EASY;

    @Lob
    @Column(nullable = false, columnDefinition = "LONGTEXT")
    private String description;

    @Column(name = "example_input", columnDefinition = "TEXT")
    private String exampleInput;

    @Column(name = "example_output", columnDefinition = "TEXT")
    private String exampleOutput;

    @Column(columnDefinition = "TEXT")
    private String constraints;

    @Column(columnDefinition = "TEXT")
    private String hints;

    @Column(name = "expected_approach", columnDefinition = "TEXT")
    private String expectedApproach;

    @Column(name = "time_complexity", length = 100)
    private String timeComplexity = "O(n)";

    @Column(name = "space_complexity", length = 100)
    private String spaceComplexity = "O(1)";

    @Lob
    @Column(name = "starter_code_java", columnDefinition = "LONGTEXT")
    private String starterCodeJava;

    @Lob
    @Column(name = "starter_code_js", columnDefinition = "LONGTEXT")
    private String starterCodeJs;

    @Lob
    @Column(name = "test_cases_json", columnDefinition = "LONGTEXT")
    private String testCasesJson;

    public CodingChallenge() {
    }

    public CodingChallenge(String title, String slug, SkillCategory category, String topic, Difficulty difficulty, String description, String exampleInput, String exampleOutput, String constraints, String hints, String expectedApproach, String timeComplexity, String spaceComplexity, String starterCodeJava, String starterCodeJs, String testCasesJson) {
        this.title = title;
        this.slug = slug;
        this.category = category;
        this.topic = topic;
        this.difficulty = difficulty;
        this.description = description;
        this.exampleInput = exampleInput;
        this.exampleOutput = exampleOutput;
        this.constraints = constraints;
        this.hints = hints;
        this.expectedApproach = expectedApproach;
        this.timeComplexity = timeComplexity;
        this.spaceComplexity = spaceComplexity;
        this.starterCodeJava = starterCodeJava;
        this.starterCodeJs = starterCodeJs;
        this.testCasesJson = testCasesJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExampleInput() {
        return exampleInput;
    }

    public void setExampleInput(String exampleInput) {
        this.exampleInput = exampleInput;
    }

    public String getExampleOutput() {
        return exampleOutput;
    }

    public void setExampleOutput(String exampleOutput) {
        this.exampleOutput = exampleOutput;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getHints() {
        return hints;
    }

    public void setHints(String hints) {
        this.hints = hints;
    }

    public String getExpectedApproach() {
        return expectedApproach;
    }

    public void setExpectedApproach(String expectedApproach) {
        this.expectedApproach = expectedApproach;
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

    public String getStarterCodeJava() {
        return starterCodeJava;
    }

    public void setStarterCodeJava(String starterCodeJava) {
        this.starterCodeJava = starterCodeJava;
    }

    public String getStarterCodeJs() {
        return starterCodeJs;
    }

    public void setStarterCodeJs(String starterCodeJs) {
        this.starterCodeJs = starterCodeJs;
    }

    public String getTestCasesJson() {
        return testCasesJson;
    }

    public void setTestCasesJson(String testCasesJson) {
        this.testCasesJson = testCasesJson;
    }
}
