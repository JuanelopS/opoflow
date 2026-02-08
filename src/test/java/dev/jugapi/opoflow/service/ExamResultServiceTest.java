package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.ExamResult;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.stats.RankingEntry;
import dev.jugapi.opoflow.model.stats.UserStatistics;
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
    @DisplayName("Should call repository save when service save is called")
    void testSave() {
        ExamResult result = new ExamResult(new User(UUID.randomUUID()), LocalDateTime.now(),
                OppositionTopic.TAI_ALL, 10, 0, 0, 10.0);

        examService.save(result);

        Mockito.verify(repository).save(result);
    }


    @Test
    @DisplayName("Should return 3 exams ordered by date (most recent first)")
    void testFindByUser() {
        OppositionTopic topic = OppositionTopic.TAI_ALL;

        User user = new User("User 1");
        ExamResult result1 = new ExamResult(user,
                LocalDateTime.of(2026, 2, 8, 10, 0),
                topic, 10, 0, 0, 10);
        ExamResult result2 = new ExamResult(user,
                LocalDateTime.of(2026, 2, 1, 10, 0),
                topic, 10, 0, 0, 10);
        ExamResult result3 = new ExamResult(user,
                LocalDateTime.of(2026, 1, 15, 10, 0),
                topic, 10, 0, 0, 10);
        ExamResult result4 = new ExamResult(user,
                LocalDateTime.of(2025, 2, 8, 10, 0),
                topic, 10, 0, 0, 10);
        ExamResult result5 = new ExamResult(user,
                LocalDateTime.of(2024, 2, 8, 10, 0),
                topic, 10, 0, 0, 10);

        Mockito.when(repository.findByUser(user)).thenReturn(
                List.of(result1, result2, result3, result4, result5));

        List<ExamResult> results = examService.findByUser(user, 3);

        assertEquals(3, results.size());
        assertEquals(result1.getDate(), results.get(0).getDate());
        assertEquals(result3.getDate(), results.get(2).getDate());
    }

    @Test
    @DisplayName("Should return an empty list")
    void testGetRankingByTopicEmptyList() {
        OppositionTopic topic = OppositionTopic.TAI_ALL;

        Mockito.when(repository.findAll()).thenReturn(List.of());

        List<RankingEntry> ranking = examService.getRankingByTopic(topic);

        assertTrue(ranking.isEmpty(), "The ranking should be empty");
    }

    @Test
    @DisplayName("Should return 2 and 9.0")
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
    void testGetRankingByTopic() {
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

    @Test
    @DisplayName("Should return 3 (total), average score 7.0 and specific average score 7.0")
    void testGetUserStats() {
        OppositionTopic topicTai = OppositionTopic.TAI_ALL;
        OppositionTopic topicOther = OppositionTopic.ADVO_ALL;

        User user = new User("user");

        ExamResult tai1 = new ExamResult(user, null, LocalDateTime.now(),
                topicTai, 10, 10, 0, 10.0);
        ExamResult tai2 = new ExamResult(user, null, LocalDateTime.now(),
                topicTai, 18, 2, 0, 4.0);
        ExamResult advo1 = new ExamResult(user, null, LocalDateTime.now(),
                topicOther, 18, 2, 0, 7.0);

        Mockito.when(repository.findByUser(user)).thenReturn(List.of(tai1, tai2, advo1));

        UserStatistics stats = examService.getUserStats(user);

        assertEquals(3, stats.getTotalExams());
        assertEquals(7.0, stats.getAverageScore());
        assertEquals(7.0, stats.getStats().get(topicTai));
    }

    @Test
    @DisplayName("Should call repository delete when service delete is called")
    void testDelete() {
        UUID id = UUID.randomUUID();

        examService.delete(id);

        Mockito.verify(repository).delete(id);
    }
}

