package dev.jugapi.opoflow;

import dev.jugapi.opoflow.model.*;
import dev.jugapi.opoflow.repository.FileQuestionRepository;
import dev.jugapi.opoflow.repository.QuestionRepository;
import dev.jugapi.opoflow.service.QuestionService;
import dev.jugapi.opoflow.ui.ConsoleUI;


public class Main {
    public static void main(String[] args) {

        QuestionRepository repository = new FileQuestionRepository();
        QuestionService service = new QuestionService(repository);
        ConsoleUI ui = new ConsoleUI(service);
        ui.start();
    }
}
