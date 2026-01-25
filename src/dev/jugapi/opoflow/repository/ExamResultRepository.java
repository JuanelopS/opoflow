package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.user.User;

import java.util.List;

public interface ExamResultRepository {

    void save(ExamResult result);
    List<ExamResult> getResultsByUser (User user);
}
