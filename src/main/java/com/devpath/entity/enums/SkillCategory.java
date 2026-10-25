package com.devpath.entity.enums;

public enum SkillCategory {
    PROGRAMMING_FUNDAMENTALS("Programming Fundamentals"),
    DATA_STRUCTURES_AND_ALGORITHMS("Data Structures & Algorithms"),
    APTITUDE_AND_LOGICAL("Aptitude & Logical Reasoning"),
    CS_FUNDAMENTALS("Computer Science Fundamentals"),
    TECH_INTERVIEW_PREP("Technical Interview Preparation");

    private final String displayName;

    SkillCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
