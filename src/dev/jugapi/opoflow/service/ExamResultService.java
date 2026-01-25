package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.ExamResultRepository;

import java.util.List;

public class ExamResultService {

    private final ExamResultRepository repository;

    public ExamResultService(ExamResultRepository repository) {
        this.repository = repository;
    }

    public void save(ExamResult result) {
        repository.save(result);
    }

    public List<ExamResult> findByUser(User user){
        return repository.findByUser(user);
    }
}
