package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.exam.ExamResult;

import java.io.*;

public class FileExamResultRepository implements ExamResultRepository {

    private final String resultsFilename;

    public FileExamResultRepository(String resultsFilename) {
        this.resultsFilename = resultsFilename;
    }

    @Override
    public void saveResult(ExamResult result) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(resultsFilename, true))) {
            String line = String.format("%s;%s;%s;%d;%d;%d;%.2f",
                    result.getUser().getId(),
                    result.getUser().getName(),
                    result.getTopic().name(),
                    result.getCorrect(),
                    result.getIncorrect(),
                    result.getUnanswered(),
                    result.getScore());
            writer.println(line);
        } catch (
                IOException e) {
            System.err.println("Se ha producido un error guardando el resultado del test.");
            throw new RuntimeException(e);
        }
    }
}
