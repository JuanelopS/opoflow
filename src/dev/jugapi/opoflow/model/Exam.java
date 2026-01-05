package dev.jugapi.opoflow.model;

import java.util.List;

public class Exam {

    private String title;
    private List<Question> questions;
    private int currentQuestionIndex;
    private float score;
    private OppositionTopic topic;
    /*
    TODO: Correct and incorrect: because each competitive exam is graded differently, so that in the future a method
     will
    give me the final score according to the type of exam. For now, I will only use +1 for the score.
    */
    private int correct;
    private int incorrect;

    public Exam(String title, List<Question> questions, OppositionTopic topic) {
        this.title = title;
        this.questions = questions;
        this.currentQuestionIndex = 0;
        this.score = 0.0f;
        this.topic = topic;
        this.correct = 0;
        this.incorrect = 0;
    }

    public String getTitle() {
        return title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
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

    public Question getNextQuestion(){
        if(!hasMoreQuestions()){
            return null;
        }
        return questions.get(currentQuestionIndex++);
    }

    public boolean hasMoreQuestions(){
        return currentQuestionIndex < questions.size();
    }

    public void registerAnswer(boolean isCorrect){
        if(isCorrect){
            correct++;
        } else {
            incorrect++;
        }
    }
}
