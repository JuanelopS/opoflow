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
    @Override
    public List<Question> retrieveQuestions() {

        List<Question> questions = new ArrayList<>();
        File archive = new File("questions2.txt");

        try (Scanner reader = new Scanner(archive)) {
            while (reader.hasNextLine()) {

                String line = reader.nextLine();
                if(line.isBlank()) continue;

                String[] segments = line.split(";");
                if (segments.length < 7) {
                    System.err.println(line + ": Línea mal formateada. Revisa el formato del archivo de preguntas");
                    continue;
                }

                String prompt = segments[1];
                int correctOption = Integer.parseInt(segments[6].trim());
                List<Option> options = new ArrayList<>();
                for (int i = 0; i < 4; i++) {
                    String textOption = segments[i + 2].trim();
                    boolean isCorrect = (i + 1 == correctOption);
                    options.add(new Option(textOption, isCorrect));
                }
                OppositionTopic topic = OppositionTopic.valueOf(segments[0].toUpperCase().trim());

                Question question = new Question(prompt, options, topic);
                questions.add(question);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Archivo de preguntas no encontrado: " + e.getMessage());
        }

        return questions;
    }
}
