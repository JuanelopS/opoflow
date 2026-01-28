package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.stats.UserStatistics;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.ExamResultRepository;

import java.util.DoubleSummaryStatistics;
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

    public UserStatistics getUserStats(User user){
        List<ExamResult> stats = repository.findByUser(user);
        if(stats.isEmpty()){
            return new UserStatistics(0, 0, 0);
        }

        int totalExams = stats.size();
        DoubleSummaryStatistics summaryStatistics = stats.stream()
                .mapToDouble(ExamResult::getScore)
                .summaryStatistics();
        double averageScore = summaryStatistics.getAverage();
        double maxScore = summaryStatistics.getMax();

        return new UserStatistics(totalExams, averageScore, maxScore);
    }
}
