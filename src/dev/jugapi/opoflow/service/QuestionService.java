package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.Exam;
import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;
import dev.jugapi.opoflow.repository.QuestionRepository;

import java.util.List;

public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getExamQuestions() {
        return repository.retrieveQuestions();
    }

    /* This method create a new Exam for a UI filtering by topic (see enums in model package) */
    public Exam createNewExam(OppositionTopic topic){
        List<Question> allQuestions = getExamQuestions();
        List<Question> filtered = allQuestions.stream()
                .filter(q -> topic.includes(q.getTopic()))
                .toList();
        return new Exam(topic.getDescription(), filtered, topic);
    }

    public OppositionTopic[] getAllTopics(){
        return OppositionTopic.values();
    }

    public boolean checkAnswer(Question question, int response) {
        List<Option> options = question.getOptions();
        if (response >= options.size() || response < 0) {
            return false;
        }
        return options.get(response).isCorrect();
    }
}
