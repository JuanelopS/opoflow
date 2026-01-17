package dev.jugapi.opoflow.model.exam;

import java.time.LocalDate;

public class ExamResult {
    private OppositionTopic topic;
    private int correct;
    private int incorrect;
    private int unanswered;
    private double score;
    private LocalDate date;

    // TODO: add user to "log"

    public ExamResult(OppositionTopic topic, int correct, int incorrect, int unanswered, double score) {
        this.topic = topic;
        this.correct = correct;
        this.incorrect = incorrect;
        this.unanswered = unanswered;
        this.score = score;
        this.date = LocalDate.now();
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
}
