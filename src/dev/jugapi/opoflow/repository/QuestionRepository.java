package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.ExamResult;
import dev.jugapi.opoflow.model.Question;

import java.util.List;

public interface QuestionRepository {

    List<Question> retrieveQuestions();
    void saveResult(ExamResult result);
}
