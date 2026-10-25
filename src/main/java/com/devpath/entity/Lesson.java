package com.devpath.entity;

import com.devpath.entity.enums.Difficulty;
import com.devpath.entity.enums.SkillCategory;
import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "learning_path_id", nullable = false)
    private LearningPath learningPath;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 150)
    private String slug;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SkillCategory category;

    @Column(nullable = false, length = 100)
    private String topic;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Difficulty difficulty = Difficulty.BEGINNER;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @Column(columnDefinition = "TEXT")
    private String prerequisites;

    @Column(name = "key_takeaways", columnDefinition = "TEXT")
    private String keyTakeaways;

    @Column(name = "read_time_minutes")
    private Integer readTimeMinutes = 10;

    @Column(name = "order_index")
    private Integer orderIndex = 0;

    @Column(nullable = false)
    private Boolean published = true;

    public Lesson() {
    }

    public Lesson(LearningPath learningPath, String title, String slug, SkillCategory category, String topic, Difficulty difficulty, String summary, String content, String prerequisites, String keyTakeaways, Integer readTimeMinutes, Integer orderIndex) {
        this.learningPath = learningPath;
        this.title = title;
        this.slug = slug;
        this.category = category;
        this.topic = topic;
        this.difficulty = difficulty;
        this.summary = summary;
        this.content = content;
        this.prerequisites = prerequisites;
        this.keyTakeaways = keyTakeaways;
        this.readTimeMinutes = readTimeMinutes;
        this.orderIndex = orderIndex;
        this.published = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LearningPath getLearningPath() {
        return learningPath;
    }

    public void setLearningPath(LearningPath learningPath) {
        this.learningPath = learningPath;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(String prerequisites) {
        this.prerequisites = prerequisites;
    }

    public String getKeyTakeaways() {
        return keyTakeaways;
    }

    public void setKeyTakeaways(String keyTakeaways) {
        this.keyTakeaways = keyTakeaways;
    }

    public Integer getReadTimeMinutes() {
        return readTimeMinutes;
    }

    public void setReadTimeMinutes(Integer readTimeMinutes) {
        this.readTimeMinutes = readTimeMinutes;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }
}
