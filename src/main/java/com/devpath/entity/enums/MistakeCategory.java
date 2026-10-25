package com.devpath.entity.enums;

public enum MistakeCategory {
    CONCEPT_MISUNDERSTANDING("Concept Misunderstanding", "Gaps in fundamental principles or core theory."),
    INCORRECT_LOGIC("Incorrect Logic", "Logical progression or state management flaw in the solution."),
    BOUNDARY_CASE_ERROR("Boundary-Case Error", "Failure to handle edge cases such as empty input, zero, or max limits."),
    SYNTAX_OR_TYPE_TRAP("Syntax / Type Trap", "Subtle language nuances like integer overflow, pass-by-value vs reference, or operator precedence."),
    TIME_SPACE_INEFFICIENCY("Time / Space Inefficiency", "Algorithm exceeded time complexity or memory budget.");

    private final String label;
    private final String description;

    MistakeCategory(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }
}
