package dev.jugapi.opoflow.model;

public enum Opposition {
    TAI_AGE_PART1(1, (double) 1/3);

    private final double correct;
    private final double incorrect;

    Opposition(double correct, double incorrect) {
        this.correct = correct;
        this.incorrect = incorrect;
    }

    public double getCorrect() {
        return correct;
    }

    public double getIncorrect() {
        return incorrect;
    }
}
