package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.stats.UserStatistics;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.ExamResultRepository;

import java.util.*;

import static java.util.stream.Collectors.averagingDouble;
import static java.util.stream.Collectors.groupingBy;

public class ExamResultService {

    private final ExamResultRepository repository;

    public ExamResultService(ExamResultRepository repository) {
        this.repository = repository;
    }

    public void save(ExamResult result) {
        repository.save(result);
    }

    public List<ExamResult> findByUser(User user){
        return repository.findByUser(user)
                .stream()
                .sorted(Comparator.comparing(ExamResult::getDate).reversed())
                .toList();
    }

    public List<ExamResult> findByUser(User user, int limit){
        return repository.findByUser(user)
                .stream()
                .sorted(Comparator.comparing(ExamResult::getDate).reversed())
                .limit(limit)
                .toList();
    }

    public UserStatistics getUserStats(User user){
        List<ExamResult> stats = repository.findByUser(user);
        if(stats.isEmpty()){
            return new UserStatistics(0, 0, 0, Collections.emptyMap());
        }

        int totalExams = stats.size();
        DoubleSummaryStatistics summaryStatistics = stats.stream()
                .mapToDouble(ExamResult::getScore)
                .summaryStatistics();
        double averageScore = summaryStatistics.getAverage();
        double maxScore = summaryStatistics.getMax();

        Map<OppositionTopic, Double> statsByTopic = stats.stream()
                .collect(groupingBy(ExamResult::getTopic, averagingDouble(ExamResult::getScore)));

        return new UserStatistics(totalExams, averageScore, maxScore, statsByTopic);
    }

    public void delete(UUID id){
        repository.delete(id);
    }
}
