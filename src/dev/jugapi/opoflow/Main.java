package dev.jugapi.opoflow;

import dev.jugapi.opoflow.repository.ExamResultRepository;
import dev.jugapi.opoflow.repository.FileExamResultRepository;
import dev.jugapi.opoflow.repository.FileQuestionRepository;
import dev.jugapi.opoflow.repository.QuestionRepository;
import dev.jugapi.opoflow.service.ExamResultService;
import dev.jugapi.opoflow.service.QuestionService;
import dev.jugapi.opoflow.ui.ConsoleUI;


public class Main {
    public static void main(String[] args) {

        QuestionRepository questionRepository = new FileQuestionRepository("questions3.txt");
        ExamResultRepository examResultRepository = new FileExamResultRepository("results.txt");
        QuestionService questionService = new QuestionService(questionRepository);
        ExamResultService examResultService = new ExamResultService(examResultRepository);
        ConsoleUI ui = new ConsoleUI(questionService, examResultService);
        ui.start();
    }
}
