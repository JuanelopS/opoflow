package dev.jugapi.opoflow.ui;

import dev.jugapi.opoflow.model.Exam;
import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;
import dev.jugapi.opoflow.service.QuestionService;

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
            int response;
            do {
                response = translateResponse(q.getOptions().size());
                if (response == -1) {   // -1 because translateResponse / indexOf return
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
        System.out.println("\nSelecciona la oposición/bloque: ");

        int index = 1;
        for (OppositionTopic ot : topics) {
            System.out.println(index + ".- " + ot.name() + " -> " + ot.getDescription());
            index++;
        }

        int response = 0;
        boolean isValid = false;
        do {
            System.out.print("Opción: ");
            try {
                response = Integer.parseInt(kb.nextLine());
                if(response < 1 || response > topics.length){
                    throw new IllegalArgumentException();
                }
                isValid = true;
            } catch (IllegalArgumentException e) {   // parseInt && outside a certain range (topics.length)
                System.out.println(ConsoleUIColor.RED + "El valor introducido no es válido" + ConsoleUIColor.RESET);
                response = 0;  // continue in loop
            }
        } while (!isValid);

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

    private int translateResponse(int options) {
        String letter = kb.nextLine().toLowerCase().trim();
        if (letter.isEmpty()) {
            return -2;
        } else {
            String validLetters = "abcdefghijklmnopqrstuvwxyz";  //TODO: regex¿?
            int response = validLetters.indexOf(letter);
            if (response == -1 || response >= options) {
                return -1;
            } else return response;
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
