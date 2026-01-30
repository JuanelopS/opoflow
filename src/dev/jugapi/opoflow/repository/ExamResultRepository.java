package dev.jugapi.opoflow.repository;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.user.User;

import java.util.List;
import java.util.UUID;

public interface ExamResultRepository {

    void save(ExamResult result);
    void delete(UUID uuid);
    List<ExamResult> findByUser(User user);
    List<ExamResult> findAll();
}
