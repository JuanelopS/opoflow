package dev.jugapi.opoflow.model;

public enum Opposition {
    TAI_AGE_PART1(1, (double) 1/3);

    private final double hit;
    private final double error;

    Opposition(double hit, double error) {
        this.hit = hit;
        this.error = error;
    }

    public double getHit() {
        return hit;
    }

    public double getError() {
        return error;
    }
}
