package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileQuestionRepository implements QuestionRepository {

    private String fileName;

    private static final int INDEX_TOPIC = 0;
    private static final int INDEX_PROMPT = 1;
    private static final int INDEX_OPTIONS_START = 2;
    private static final int INDEX_CORRECT_ANSWER = 6;

    public FileQuestionRepository(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Question> retrieveQuestions() {

        List<Question> questions = new ArrayList<>();
        File archive = new File(this.fileName);

        try (Scanner reader = new Scanner(archive)) {
            int cont = 0;
            while (reader.hasNextLine()) {
                cont++;
                String line = reader.nextLine();
                if (line.isBlank()) continue;

                String[] segments = line.split(";");
                if (segments.length < 7) {
                    formatFileErrorMsg(cont, archive);
                    continue;
                }

                int correctOption;
                String prompt = segments[INDEX_PROMPT];
                try {
                    correctOption = Integer.parseInt(segments[INDEX_CORRECT_ANSWER].trim());
                } catch (NumberFormatException e) {
                    formatFileErrorMsg(cont, archive);
                    continue;
                }

                List<Option> options = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    String textOption = segments[i + INDEX_OPTIONS_START].trim();
                    boolean isCorrect = (i + 1 == correctOption);
                    options.add(new Option(textOption, isCorrect));
                }

                OppositionTopic topic = null;
                try {
                    topic = OppositionTopic.valueOf(segments[INDEX_TOPIC].toUpperCase().trim());
                } catch (
                        IllegalArgumentException e) {
                    formatFileErrorMsg(cont, archive);
                    continue;
                }

                Question question = new Question(prompt, options, topic);
                questions.add(question);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Archivo de preguntas no encontrado: " + e.getMessage());
        }

        return questions;
    }

    public void formatFileErrorMsg(int cont, File archive){System.err.println("Línea " + cont +
                ": Formato incorrecto. Revisa el formato del archivo de preguntas (" +
                archive.getName() + ")");
    }
}
