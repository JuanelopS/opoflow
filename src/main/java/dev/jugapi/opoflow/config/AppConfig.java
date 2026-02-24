package dev.jugapi.opoflow.config;

import dev.jugapi.opoflow.exception.AppConfigurationException;
import dev.jugapi.opoflow.repository.*;
import dev.jugapi.opoflow.service.ExamResultService;
import dev.jugapi.opoflow.service.QuestionService;
import dev.jugapi.opoflow.service.UserService;
import dev.jugapi.opoflow.ui.ConsoleUI;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class AppConfig {
    Properties props = new Properties();

    public ConsoleUI setup() {
        try {
            FileInputStream fis = new FileInputStream("config.properties");
            props.load(fis);
        } catch (
                FileNotFoundException e) {
            throw new AppConfigurationException("¡Ups! Falta el archivo config.properties. \n" +
                    "Puedes encontrar las instrucciones aquí: https://github.com/JuanelopS/opoflow");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }

        String questions = props.getProperty("repo.questions.path");
        String results = props.getProperty("repo.results.path");
        String users = props.getProperty("repo.users.path");

        QuestionRepository questionRepository = new FileQuestionRepository(questions);
        ExamResultRepository examResultRepository = new FileExamResultRepository(results);
        UserRepository userRepository = new FileUserRepository(users);
        QuestionService questionService = new QuestionService(questionRepository);
        UserService userService = new UserService(userRepository);
        ExamResultService examResultService = new ExamResultService(examResultRepository, userService);
        ConsoleUI ui = new ConsoleUI(questionService, examResultService, userService);

        return ui;
    }
}
