package dev.jugapi.opoflow.model.exam;

import dev.jugapi.opoflow.model.user.User;

import java.time.LocalDateTime;

public class ExamResult {

    private User user;
    private LocalDateTime date;
    private OppositionTopic topic;
    private int correct;
    private int incorrect;
    private int unanswered;
    private double score;

    public ExamResult(User user, LocalDateTime date, OppositionTopic topic, int correct, int incorrect, int unanswered, double score) {
        this.user = user;
        this.date = date;
        this.topic = topic;
        this.correct = correct;
        this.incorrect = incorrect;
        this.unanswered = unanswered;
        this.score = score;
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

    public LocalDateTime getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }
}
