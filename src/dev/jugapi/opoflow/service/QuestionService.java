package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.Exam;
import dev.jugapi.opoflow.model.OppositionTopic;
import dev.jugapi.opoflow.model.Option;
import dev.jugapi.opoflow.model.Question;
import dev.jugapi.opoflow.repository.QuestionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionService {

    private final QuestionRepository repository;

    public QuestionService(QuestionRepository repository) {
        this.repository = repository;
    }

    public List<Question> getExamQuestions() {
        return repository.retrieveQuestions();
    }

    /* This method create a new Exam for a UI filtering by topic (see enums in model package) */
    public Exam createNewExam(OppositionTopic topic) {
        List<Question> allQuestions = getExamQuestions();
        List<Question> filtered = allQuestions.stream()
                .filter(q -> topic.includes(q.getTopic()))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(filtered);
        for (Question q : filtered) {
            q.shuffleOptions();
        }
        return new Exam(topic.getDescription(), filtered, topic);
    }

    public OppositionTopic[] getAllTopics() {
        return OppositionTopic.values();
    }

    public boolean checkAnswer(Question question, int response) {
        List<Option> options = question.getOptions();
        if (response >= options.size() || response < 0) {
            return false;
        }
        return options.get(response).isCorrect();
    }

    public int getQuestionCount(OppositionTopic topic) {
        long questionsCount = getExamQuestions().stream()
                .filter(q -> topic.includes(q.getTopic()))
                .count();
        return (int) questionsCount;
    }
}
