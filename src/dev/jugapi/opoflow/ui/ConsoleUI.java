package dev.jugapi.opoflow.ui;

import dev.jugapi.opoflow.model.Exam;
import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;
import dev.jugapi.opoflow.service.QuestionService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private QuestionService service;
    private Scanner kb;

    public ConsoleUI(QuestionService service) {
        this.service = service;
        this.kb = new Scanner(System.in);
    }

    public void start() {
        System.out.println("\t\t\t=== OPOFLOW ===");
        OppositionTopic topic = selectTopic();
        Exam exam = service.createNewExam(topic);

        for (Question q : exam.getQuestions()) {
            // TODO: add number to question prompt
            displayQuestion(q);
            System.out.print("Respuesta: ");
            int response;
            do
            {
                response = translateResponse(q.getOptions().size());
                if (response == -1) {
                    System.out.print(ConsoleUIColor.RED +
                            "Respuesta no válida. Por favor introduce un valor valido: " +
                            ConsoleUIColor.RESET);
                }
            } while (response == -1);

            if (response == -2) {
                System.out.println(ConsoleUIColor.BLUE + "Respuesta en blanco" + ConsoleUIColor.RESET);
                exam.registerUnanswered();
                continue;
            } else {
                boolean isCorrect = service.checkAnswer(q, response);
                exam.registerAnswer(isCorrect);
                System.out.println(isCorrect ?
                        ConsoleUIColor.GREEN + "Respuesta correcta" + ConsoleUIColor.RESET :
                        ConsoleUIColor.RED + "Respuesta incorrecta" + ConsoleUIColor.RESET);
            }
        }
        displayResults(exam);
    }

    // TODO: handle exams that do not yet have questions
    private OppositionTopic selectTopic() {
        OppositionTopic[] topics = service.getAllTopics();
        System.out.println("\nSelecciona la oposición/bloque: (nº de preguntas disponibles)");

        List<OppositionTopic> activeTopics = new ArrayList<>();
        for (OppositionTopic ot : topics) {
            if (service.getQuestionCount(ot) > 0) {
                activeTopics.add(ot);
            }
        }

        for (int i = 0; i < activeTopics.size(); i++) {
            OppositionTopic ot = activeTopics.get(i);
            System.out.println(i + 1 + ".- " + ot.name() + " -> " + ot.getDescription() +
                    " (" + service.getQuestionCount(ot) + ")");
        }

        int response = 0;
        boolean isValid = false;
        do
        {
            System.out.print("Opción: ");
            try {
                response = Integer.parseInt(kb.nextLine());
                if (response < 1 || response > activeTopics.size()) {
                    throw new IllegalArgumentException();
                }
                isValid = true;
            } catch (
                    IllegalArgumentException e) {
                System.out.println(ConsoleUIColor.RED + "El valor introducido no es válido" +
                        ConsoleUIColor.RESET);
                response = 0;  // continue in loop
            }
        } while (!isValid);

        return activeTopics.get(response - 1);
    }

    private void displayQuestion(Question question) {
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f'};
        int index = 0;
        System.out.println("\n" + question.getPrompt());
        for (Option o : question.getOptions()) {
            System.out.println(letters[index++] + ") " + o.getAnswer());
        }
    }

    private int translateResponse(int optionsCount) {
        String input = kb.nextLine().toLowerCase().trim();
        if (input.isEmpty()) {
            return -2;
        }

        char maxLetter = (char) ('a' + optionsCount - 1);
        String regex = "^[a-" + maxLetter + "]$";

        if (input.matches(regex)) {
            return input.charAt(0) - 'a';
        } else {
            return -1;
        }
    }


    private void displayResults(Exam exam) {
        exam.calculateFinalScore();
        System.out.println("Correctas: " + exam.getCorrect());
        System.out.println("Incorrectas: " + exam.getIncorrect());
        System.out.println("Sin contestar: " + exam.getUnanswered());
        System.out.printf("Puntuación: %.2f", exam.getScore());
    }
}
