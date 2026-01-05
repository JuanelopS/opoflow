package dev.jugapi.opoflow.ui;

import dev.jugapi.opoflow.model.Exam;
import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;
import dev.jugapi.opoflow.service.QuestionService;

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
            displayQuestion(q);
            System.out.print("Respuesta: ");
            int response = getResponse();
            if (response == -1) {   // -1 because getResponse / indexOf return
                System.out.println("Respuesta no válida.");
                continue;
            }
            boolean isCorrect = service.checkAnswer(q, response);
            exam.registerAnswer(isCorrect);
            System.out.println(isCorrect ? "Respuesta correcta" : "Respuesta incorrecta");
        }
        displayResults(exam);
    }

    private OppositionTopic selectTopic() {
        OppositionTopic[] topics = service.getAllTopics();
        System.out.println("\nSelecciona la oposición/bloque: ");

        int index = 1;
        for (OppositionTopic ot : topics) {
            System.out.println(index + ".- " + ot.name() + " -> " + ot.getDescription());
            index++;
        }

        int response;
        do {
            System.out.print("Opción: ");
            response = kb.nextInt();
        } while ((response < 1) || (response > topics.length));

        return topics[response - 1];
    }

    private void displayQuestion(Question question) {
        char[] letters = {'a', 'b', 'c', 'd', 'e', 'f'};
        int index = 0;
        System.out.println("\n" + question.getPrompt());
        for (Option o : question.getOptions()) {
            System.out.println(letters[index++] + ") " + o.getAnswer());
        }
    }

    private int getResponse() {
        String validLetters = "abcdef";  //todo: regex¿?
        String letter = kb.next().toLowerCase();
        return validLetters.indexOf(letter);
    }

    // TODO: clean this -> too many things (validation, prints and returns result)
    private boolean checkAnswer(Question question, int response) {
        List<Option> options = question.getOptions();
        if (response >= options.size() || response < 0) {
            System.out.println("Respuesta no válida");
            return false;
        }
        if (options.get(response).isCorrect()) {
            System.out.println("Respuesta correcta!");
        } else System.out.println("Respuesta incorrecta");
        return options.get(response).isCorrect();
    }

    private void displayResults(Exam exam) {
        System.out.println("Correctas: " + exam.getCorrect());
        System.out.println("Incorrectas: " + exam.getIncorrect());
        System.out.println("Total: " + (exam.getCorrect() - exam.getIncorrect()));
    }

}
