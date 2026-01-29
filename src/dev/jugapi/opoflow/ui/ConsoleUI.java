package dev.jugapi.opoflow.ui;

import dev.jugapi.opoflow.model.exam.*;
import dev.jugapi.opoflow.model.stats.UserStatistics;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.service.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {

    private final QuestionService questionService;
    private final ExamResultService examResultService;
    private final UserService userService;
    private final Scanner kb;
    private static final int MAX_HISTORY_ITEMS = 10;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public ConsoleUI(QuestionService questionService, ExamResultService examResultService, UserService userService) {
        this.questionService = questionService;
        this.examResultService = examResultService;
        this.userService = userService;
        this.kb = new Scanner(System.in);
    }

    public void start() {
        System.out.println(ConsoleUIColor.BLUE + "\t--- OPOFLOW ---\n" + ConsoleUIColor.RESET);
        User user = identifyUser();
        System.out.println("Bienvenido " + user.getName() + "! (id: " + user.getId() + ")");

        boolean exit = false;
        while (!exit) {

            System.out.println(ConsoleUIColor.GREEN + "\n\t--- MENÚ PRINCIPAL ---" + ConsoleUIColor.RESET);
            System.out.println(ConsoleUIColor.BLUE + "Escoge una de las siguientes opciones: " + ConsoleUIColor.RESET);
            System.out.println("1. Nuevo test");
            System.out.println("2. Ver tus estadísticas");
            System.out.println("3. Gestionar historial (borrar)");
            System.out.println("4. Salir");
            System.out.print("Selecciona una opción: ");
            String option = kb.nextLine();

            switch (option) {
                case "1" ->
                        runExam(user);
                case "2" ->
                        showStatistics(user);
                case "3" ->
                        manageHistory(user);
                case "4" ->
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
                            "Respuesta no válida. Vuelve a intentarlo: " +
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
        UserStatistics stats = examResultService.getUserStats(user);
        if (stats.getTotalExams() == 0) {
            System.out.println("\nNo tienes datos todavía.");
            return;
        }

        printTopicStats(user, stats);
        System.out.println("-----------------------------------");
        printHistory(user);
    }

    private void printTopicStats(User user, UserStatistics stats) {
        System.out.printf(ConsoleUIColor.BLUE + "%n\t--- ESTADÍSTICAS DE %s ---%n",
                user.getName().toUpperCase() + ConsoleUIColor.RESET);
        System.out.printf("Total de exámenes: %d | Media global: %.2f | Récord: %.2f %n%n",
                stats.getTotalExams(), stats.getAverageScore(), stats.getMaxScore());

        System.out.println("Rendimiento por tema: ");
        stats.getStats().forEach((topic, avg) ->
                System.out.printf("- %s: %.2f%n", topic.getDescription(), avg));
    }

    private List<ExamResult> printHistory(User user) {
        System.out.println(ConsoleUIColor.BLUE + "HISTORIAL (últimos 10):" + ConsoleUIColor.RESET);
        List<ExamResult> history = examResultService.findByUser(user, MAX_HISTORY_ITEMS);
        int count = 0;
        for (ExamResult e : history) {
            String result = String.format("%d. %s - %s(%s): %s",
                    ++count,
                    e.getDate().format(formatter),
                    e.getTopic().name(),
                    e.getTopic().getDescription(),
                    e.getScore());
            System.out.println(result);
        }
        return history;
    }

    public void manageHistory(User user) {
        System.out.println();
        List<ExamResult> history = Collections.emptyList();
        int selected;
        boolean isValid = false;
        do
        {
            history = printHistory(user);
            System.out.println(ConsoleUIColor.RED
                    + "\n¿Deseas borrar un examen del historial?"
                    + ConsoleUIColor.RESET);
            System.out.print("Pulsa 0 para volver al menú o el nº del examen que deseas borrar: ");
            try {
                selected = Integer.parseInt(kb.nextLine().trim());
                if (selected < 0 || selected > history.size()) {
                    throw new NumberFormatException();
                }
                if (selected != 0) {
                    ExamResult exam = history.get(selected - 1);

                    System.out.print(ConsoleUIColor.RED + "¿Estás seguro que deseas borrar el examen "
                            + selected
                            + " de fecha " + exam.getDate().format(formatter)
                            + " (Y/N)? ");

                    String confirmation;
                    boolean validConfirmation = false;
                    do
                    {
                        confirmation = kb.nextLine().trim().toLowerCase();
                        if (confirmation.equals("y")) {
                            examResultService.delete(exam.getId());
                            System.out.println("Borrando la opción " + selected + ".");
                            isValid = true;
                            validConfirmation = true;
                        } else if (confirmation.equals("n")) {
                            validConfirmation = true;
                            System.out.println();
                        } else
                            System.out.println("Opción no válida!");
                    } while (!validConfirmation);
                } else {
                    isValid = true;
                }
            } catch (
                    NumberFormatException e) {
                System.out.println(ConsoleUIColor.RED + "Opción no válida!" + ConsoleUIColor.RESET);
            }
        } while (!isValid);
    }
}
