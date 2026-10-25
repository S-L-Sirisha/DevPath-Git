package com.devpath.dto;

import java.util.List;
import java.util.Map;

public class RadarScoreDto {
    private Double programmingFundamentals = 0.0;
    private Double dataStructuresAndAlgorithms = 0.0;
    private Double aptitudeAndLogical = 0.0;
    private Double csFundamentals = 0.0;
    private Double techInterviewPrep = 0.0;
    private Double overallReadinessIndex = 0.0;
    private String strongestSkill;
    private String weakestSkill;
    private List<String> areasRequiringImprovement;
    private String recommendedNextActivity;
    private Boolean isDemoData = false;
    private Integer totalQuestionsAttempted = 0;
    private Integer totalCorrect = 0;
    private Map<String, Integer> categoryAttempts;

    public RadarScoreDto() {
    }

    public Double getProgrammingFundamentals() {
        return programmingFundamentals;
    }

    public void setProgrammingFundamentals(Double programmingFundamentals) {
        this.programmingFundamentals = programmingFundamentals;
    }

    public Double getDataStructuresAndAlgorithms() {
        return dataStructuresAndAlgorithms;
    }

    public void setDataStructuresAndAlgorithms(Double dataStructuresAndAlgorithms) {
        this.dataStructuresAndAlgorithms = dataStructuresAndAlgorithms;
    }

    public Double getAptitudeAndLogical() {
        return aptitudeAndLogical;
    }

    public void setAptitudeAndLogical(Double aptitudeAndLogical) {
        this.aptitudeAndLogical = aptitudeAndLogical;
    }

    public Double getCsFundamentals() {
        return csFundamentals;
    }

    public void setCsFundamentals(Double csFundamentals) {
        this.csFundamentals = csFundamentals;
    }

    public Double getTechInterviewPrep() {
        return techInterviewPrep;
    }

    public void setTechInterviewPrep(Double techInterviewPrep) {
        this.techInterviewPrep = techInterviewPrep;
    }

    public Double getOverallReadinessIndex() {
        return overallReadinessIndex;
    }

    public void setOverallReadinessIndex(Double overallReadinessIndex) {
        this.overallReadinessIndex = overallReadinessIndex;
    }

    public String getStrongestSkill() {
        return strongestSkill;
    }

    public void setStrongestSkill(String strongestSkill) {
        this.strongestSkill = strongestSkill;
    }

    public String getWeakestSkill() {
        return weakestSkill;
    }

    public void setWeakestSkill(String weakestSkill) {
        this.weakestSkill = weakestSkill;
    }

    public List<String> getAreasRequiringImprovement() {
        return areasRequiringImprovement;
    }

    public void setAreasRequiringImprovement(List<String> areasRequiringImprovement) {
        this.areasRequiringImprovement = areasRequiringImprovement;
    }

    public String getRecommendedNextActivity() {
        return recommendedNextActivity;
    }

    public void setRecommendedNextActivity(String recommendedNextActivity) {
        this.recommendedNextActivity = recommendedNextActivity;
    }

    public Boolean getIsDemoData() {
        return isDemoData;
    }

    public void setIsDemoData(Boolean demoData) {
        isDemoData = demoData;
    }

    public Integer getTotalQuestionsAttempted() {
        return totalQuestionsAttempted;
    }

    public void setTotalQuestionsAttempted(Integer totalQuestionsAttempted) {
        this.totalQuestionsAttempted = totalQuestionsAttempted;
    }

    public Integer getTotalCorrect() {
        return totalCorrect;
    }

    public void setTotalCorrect(Integer totalCorrect) {
        this.totalCorrect = totalCorrect;
    }

    public Map<String, Integer> getCategoryAttempts() {
        return categoryAttempts;
    }

    public void setCategoryAttempts(Map<String, Integer> categoryAttempts) {
        this.categoryAttempts = categoryAttempts;
    }
}
