package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.user.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

public class FileExamResultRepository implements ExamResultRepository {

    private final String resultsFilename;
    private static final int INDEX_RESULT_ID = 1;
    private static final int INDEX_RESULT_DATE = 2;
    private static final int INDEX_RESULT_TOPIC = 3;
    private static final int INDEX_RESULT_CORRECT = 4;
    private static final int INDEX_RESULT_INCORRECT = 5;
    private static final int INDEX_RESULT_UNANSWERED = 6;
    private static final int INDEX_RESULT_SCORE = 7;

    public FileExamResultRepository(String resultsFilename) {
        this.resultsFilename = resultsFilename;
    }

    @Override
    public void save(ExamResult result) {
        try {
            String line = String.format("%s;%s;%s;%s;%d;%d;%d;%.2f" + System.lineSeparator(),
                    result.getUser().getId(),
                    result.getId(),
                    result.getDate(),
                    result.getTopic().name(),
                    result.getCorrect(),
                    result.getIncorrect(),
                    result.getUnanswered(),
                    result.getScore());
            Files.writeString(
                    Path.of(resultsFilename),
                    line,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (
                IOException e) {
            System.err.println("Se ha producido un error guardando el resultado del test.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ExamResult> findByUser(User user) {
        if (!new File(resultsFilename).exists()) {
            return new ArrayList<>();
        }

        try (Stream<String> lines = Files.lines(Path.of(resultsFilename))) {
            return lines
                    .filter(line -> line.startsWith(user.getId().toString()))
                    .map(line -> {
                        try {
                            String[] divided = line.split(";");
                            UUID id = UUID.fromString(divided[INDEX_RESULT_ID]);
                            LocalDateTime date = LocalDateTime.parse(divided[INDEX_RESULT_DATE]);
                            OppositionTopic topic = OppositionTopic.valueOf(divided[INDEX_RESULT_TOPIC]);
                            int correct = Integer.parseInt(divided[INDEX_RESULT_CORRECT]);
                            int incorrect = Integer.parseInt(divided[INDEX_RESULT_INCORRECT]);
                            int unanswered = Integer.parseInt(divided[INDEX_RESULT_UNANSWERED]);
                            double score = Double.parseDouble(divided[INDEX_RESULT_SCORE]);
                            return new ExamResult(user, id, date, topic, correct, incorrect, unanswered, score);
                        } catch (
                                Exception e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();

        } catch (
                IOException e) {
            return new ArrayList<>();
        }
    }
}
