package dev.jugapi.opoflow.model.exam;

import dev.jugapi.opoflow.model.user.User;

import java.time.LocalDate;

public class ExamResult {
    private OppositionTopic topic;
    private int correct;
    private int incorrect;
    private int unanswered;
    private double score;
    private LocalDate date;
    private User user;

    public ExamResult(OppositionTopic topic, int correct, int incorrect, int unanswered, double score, User user) {
        this.topic = topic;
        this.correct = correct;
        this.incorrect = incorrect;
        this.unanswered = unanswered;
        this.score = score;
        this.date = LocalDate.now();
        this.user = user;
    }

    public OppositionTopic getTopic() {
        return topic;
    }

    public int getCorrect() {
        return correct;
    }

    public int getIncorrect() {
        return incorrect;
    }

    public int getUnanswered() {
        return unanswered;
    }

    public double getScore() {
        return score;
    }

    public LocalDate getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
