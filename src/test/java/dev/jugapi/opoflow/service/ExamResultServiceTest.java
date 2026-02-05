package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.stats.RankingEntry;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.ExamResultRepository;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ExamResultServiceTest {

    @Mock
    private ExamResultRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ExamResultService examService;

    @Test
    @DisplayName("Should return an empty list")
    void testGetRankingByTopicEmptyList() {
        OppositionTopic topic = OppositionTopic.TAI_ALL;

        Mockito.when(repository.findAll()).thenReturn(List.of());

        List<RankingEntry> ranking = examService.getRankingByTopic(topic);

        assertTrue(ranking.isEmpty(), "The ranking should be empty");
    }

    @Test
    @DisplayName("Ranking size should be 2 and top score 9.0")
    void testGetRankingByTopicSizeAndScore() {
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

    @Test
    @DisplayName("Should return 2 after filtering by topic")
    void testGetRankingByTopic(){
        OppositionTopic topicTai = OppositionTopic.TAI_ALL;
        OppositionTopic topicOther = OppositionTopic.ADVO_ALL;

        ExamResult tai1 = new ExamResult(new User(UUID.randomUUID()), null, LocalDateTime.now(),
                topicTai, 10, 10, 0, 5.0);
        ExamResult tai2 = new ExamResult(new User(UUID.randomUUID()), null, LocalDateTime.now(),
                topicTai, 18, 2, 0, 9.0);
        ExamResult advo1 = new ExamResult(new User(UUID.randomUUID()), null, LocalDateTime.now(),
                topicOther, 18, 2, 0, 9.0);


        Mockito.when(repository.findAll()).thenReturn(List.of(tai1, tai2, advo1));

        List<RankingEntry> ranking = examService.getRankingByTopic(topicTai);

        assertEquals(2, ranking.size());
    }
}

