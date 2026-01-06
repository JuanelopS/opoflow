package dev.jugapi.opoflow.ui;

public enum ConsoleUIColor {
    GREEN("\u001B[32m"),
    RED("\u001B[31m"),
    BLUE("\u001B[34m"),
    RESET("\u001B[0m");

    private final String colorCode;

    ConsoleUIColor(String colorCode){
        this.colorCode = colorCode;
    }

    @Override
    public String toString() {
        return colorCode;
    }
}
