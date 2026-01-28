package dev.jugapi.opoflow.model.stats;

import dev.jugapi.opoflow.model.exam.OppositionTopic;

import java.util.Map;

public class UserStatistics {

    private int totalExams;
    private double averageScore;
    private double maxScore;
    private Map<OppositionTopic, Double> stats;  //TODO: statistics by topic

    public UserStatistics(int totalExams, double averageScore, double maxScore) {
        this.totalExams = totalExams;
        this.averageScore = averageScore;
        this.maxScore = maxScore;
    }

    public int getTotalExams() {
        return totalExams;
    }

    public void setTotalExams(int totalExams) {
        this.totalExams = totalExams;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }
}
