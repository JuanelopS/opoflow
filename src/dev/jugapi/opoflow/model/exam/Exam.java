package dev.jugapi.opoflow.model.exam;

import dev.jugapi.opoflow.model.user.User;

import java.util.List;

public class Exam {

    private String title;
    private List<Question> questions;
    private int currentQuestionIndex;
    private double score;
    private OppositionTopic topic;
    private final User user;

    private int correct;
    private int incorrect;
    private int unanswered;

    public Exam(String title, List<Question> questions, OppositionTopic topic, User user) {
        this.title = title;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0.0f;
        this.topic = topic;
        this.correct = 0;
        this.incorrect = 0;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
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

    public User getUser(){
        return user;
    }

    public Question getNextQuestion() {
        if (!hasMoreQuestions()) {
            return null;
        }
        return questions.get(currentQuestionIndex++);
    }

    public boolean hasMoreQuestions() {
        return currentQuestionIndex < questions.size();
    }

    public void registerAnswer(boolean isCorrect) {
        if (isCorrect) {
            correct++;
        } else {
            incorrect++;
        }
    }

    public void registerUnanswered() {
        unanswered++;
    }

    public void calculateFinalScore(){
        double hits = correct * topic.getOpposition().getCorrect();
        double errors = incorrect * topic.getOpposition().getIncorrect();
        setScore(hits - errors);
    }

    public ExamResult finish(){
        this.calculateFinalScore();
        return new ExamResult(this.topic, this.correct, this.incorrect, this.unanswered, this.score, this.user);
    }
}
