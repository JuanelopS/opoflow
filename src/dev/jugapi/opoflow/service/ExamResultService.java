package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.repository.ExamResultRepository;

public class ExamResultService {

    private final ExamResultRepository repository;

    public ExamResultService(ExamResultRepository repository) {
        this.repository = repository;
    }

    public void save(ExamResult result) {
        repository.save(result);
    }
}
