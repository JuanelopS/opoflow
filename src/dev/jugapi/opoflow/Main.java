package dev.jugapi.opoflow;

import dev.jugapi.opoflow.repository.*;
import dev.jugapi.opoflow.service.*;
import dev.jugapi.opoflow.ui.ConsoleUI;


public class Main {
    public static void main(String[] args) {

        QuestionRepository questionRepository = new FileQuestionRepository("questions3.txt");
        ExamResultRepository examResultRepository = new FileExamResultRepository("results.txt");
        UserRepository userRepository = new FileUserRepository();
        QuestionService questionService = new QuestionService(questionRepository);
        UserService userService = new UserService(userRepository);
        ExamResultService examResultService = new ExamResultService(examResultRepository, userService);
        ConsoleUI ui = new ConsoleUI(questionService, examResultService, userService);
        ui.start();
    }
}
