package dev.jugapi.opoflow.ui;

import dev.jugapi.opoflow.model.exam.*;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.service.ExamResultService;
import dev.jugapi.opoflow.service.QuestionService;
import dev.jugapi.opoflow.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final QuestionService questionService;
    private final ExamResultService examResultService;
    private final UserService userService;
    private final Scanner kb;

    public ConsoleUI(QuestionService questionService, ExamResultService examResultService, UserService userService) {
        this.questionService = questionService;
        this.examResultService = examResultService;
        this.userService = userService;
        this.kb = new Scanner(System.in);
    }

    public void start() {
        System.out.println("--- OPOFLOW ---\n");
        User user = identifyUser();
        System.out.println("Bienvenido " + user.getName() + "!");

        boolean exit = false;
        while (!exit) {

            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println(ConsoleUIColor.BLUE + "Escoge una de las siguientes opciones: " + ConsoleUIColor.RESET);
            System.out.println("1. Nuevo test");
            System.out.println("2. Ver tus estadísticas");
            System.out.println("3. Salir");
            System.out.print("Selecciona una opción: ");
            String option = kb.nextLine();

            switch (option) {
                case "1" ->
                        runExam(user);
                case "2" ->
                        showStatistics(user);
                case "3" ->
                        exit = true;
                default ->
                        System.out.println(ConsoleUIColor.RED + "Opción incorrecta!" + ConsoleUIColor.RESET);
            }
        }
    }

    public void runExam(User user) {
        OppositionTopic topic = selectTopic();
        Exam exam = questionService.createNewExam(topic, user);
        List<Question> questions = exam.getQuestions();
        for (int i = 0; i < questions.size(); i++) {
            displayQuestion(questions.get(i), i + 1);
            System.out.print("Respuesta: ");
            int response;
            do
            {
                response = translateResponse(questions.get(i).getOptions().size());
                if (response == -1) {
                    System.out.print(ConsoleUIColor.RED +
                            "Respuesta no válida. Por favor introduce un valor valido: " +
                            ConsoleUIColor.RESET);
                }
            } while (response == -1);

            if (response == -2) {
                System.out.println(ConsoleUIColor.BLUE + "Respuesta en blanco" + ConsoleUIColor.RESET);
                exam.registerUnanswered();
            } else {
                boolean isCorrect = questionService.checkAnswer(questions.get(i), response);
                exam.registerAnswer(isCorrect);
                System.out.println(isCorrect ?
                        ConsoleUIColor.GREEN + "Respuesta correcta" + ConsoleUIColor.RESET :
                        ConsoleUIColor.RED + "Respuesta incorrecta" + ConsoleUIColor.RESET);
            }
        }

        ExamResult result = exam.finish();
        examResultService.save(result);
        displayResults(result);
    }

    private User identifyUser() {
        String name;
        do
        {
            System.out.print("Introduce tu nombre: ");
            name = kb.nextLine().trim();
        } while (name.isEmpty());
        return userService.loginOrRegister(name);
    }

    private OppositionTopic selectTopic() {
        OppositionTopic[] topics = questionService.getAllTopics();
        System.out.println(ConsoleUIColor.BLUE +
                "\nSelecciona la oposición/bloque: (nº de preguntas disponibles)"
                + ConsoleUIColor.RESET);

        List<OppositionTopic> activeTopics = new ArrayList<>();
        for (OppositionTopic ot : topics) {
            if (questionService.getQuestionCount(ot) > 0) {
                activeTopics.add(ot);
            }
        }

        for (int i = 0; i < activeTopics.size(); i++) {
            OppositionTopic ot = activeTopics.get(i);
            System.out.println(i + 1 + ".- " + ot.name() + " -> " + ot.getDescription() +
                    " (" + questionService.getQuestionCount(ot) + ")");
        }

        int response;
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

    private void displayQuestion(Question question, int index) {
        System.out.println("\n" + index + ". " + question.getPrompt());

        List<Option> options = question.getOptions();
        for (int j = 0; j < options.size(); j++) {
            char letter = (char) ('a' + j);  // int to char 0->a, 1->b...
            System.out.println("   " + letter + ") " + options.get(j).getAnswer());
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

    private void displayResults(ExamResult result) {
        System.out.println(ConsoleUIColor.BLUE + "RESULTADOS: " + ConsoleUIColor.RESET);
        System.out.println("\nCorrectas: " + result.getCorrect());
        System.out.println("Incorrectas: " + result.getIncorrect());
        System.out.println("Sin contestar: " + result.getUnanswered());
        System.out.printf("Puntuación: %.2f", result.getScore());
        System.out.println();
    }

    private void showStatistics(User user) {
        List<ExamResult> list = examResultService.findByUser(user);

        if (list.isEmpty()) {
            System.out.println("No hay estadísticas registradas para tu usuario");
        } else {
            for (ExamResult e : list) {
                System.out.println(e.getDate() + " - " + e.getTopic().name() + ": " + e.getScore());
            }
        }
    }
}
