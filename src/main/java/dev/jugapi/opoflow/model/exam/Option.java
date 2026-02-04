package dev.jugapi.opoflow.model.exam;

public class Option {

    private String answer;
    private boolean isCorrect;

    public Option(String answer, boolean isCorrect) {
        this.answer = answer;
        this.isCorrect = isCorrect;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }
}
