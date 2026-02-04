package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.stats.RankingEntry;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.ExamResultRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ExamResultServiceTest {

    @Mock
    private ExamResultRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExamResultService examService;

    @Test
    void getRankingByTopic_shouldSortByAvgScore() {
        OppositionTopic topic = OppositionTopic.TAI_ALL;

        User dummyUser1 = new User(UUID.randomUUID());
        User dummyUser2 = new User(UUID.randomUUID());

        ExamResult lowScoreResult = new ExamResult(dummyUser1, null, LocalDateTime.now(),
                topic, 10, 10, 0, 5.0);
        ExamResult highScoreResult = new ExamResult(dummyUser2, null, LocalDateTime.now(),
                topic, 18, 2, 0, 9.0);

        Mockito.when(repository.findAll())
                .thenReturn(List.of(lowScoreResult, highScoreResult));

        List<RankingEntry> ranking = examService.getRankingByTopic(topic);

        assertEquals(2, ranking.size(),
                "The ranking size should be 2 (users)");
        assertEquals(9.0, ranking.get(0).averageScore(),
                "The top score should be 9.0");
    }


}