package dev.jugapi.opoflow.service;

import dev.jugapi.opoflow.model.exam.Exam;
import dev.jugapi.opoflow.model.exam.OppositionTopic;
import dev.jugapi.opoflow.model.exam.Option;
import dev.jugapi.opoflow.model.exam.Question;
import dev.jugapi.opoflow.model.user.User;
import dev.jugapi.opoflow.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestionServiceTest {

    private QuestionRepository repository;
    private QuestionService service;

    @BeforeEach
    void setup() {
        repository = Mockito.mock(QuestionRepository.class);

        List<Option> options = List.of(
                new Option("o1", false),
                new Option("o2", false),
                new Option("o3", true),
                new Option("o4", false)
        );

        List<Question> fakeQuestions = List.of(
                new Question("q1", options, OppositionTopic.TAI_ALL),
                new Question("q2", options, OppositionTopic.TAI_ALL),
                new Question("q3", options, OppositionTopic.ADVO_ALL)
        );

        Mockito.when(repository.retrieveQuestions()).thenReturn(fakeQuestions);

        service = new QuestionService(repository);
    }

    @Test
    @DisplayName("Should return 2 questions")
    void testCreateNewExam() {
        Exam result = service.createNewExam(OppositionTopic.TAI_ALL, new User("test"));
        assertEquals(2, result.getQuestions().size());
    }

    @Test
    @DisplayName("Should return true")
    void testCheckAnswerChoosingCorrectOption() {
        List<Option> options = List.of(
                new Option("False", false),
                new Option("True", true)
        );
        Question question = new Question("Is testing fun?", options,
                OppositionTopic.TAI_ALL);

        boolean result = service.checkAnswer(question, 1);
        assertTrue(result, "It should have returned true for the correct option");
    }

    @Test
    @DisplayName("Should return false")
    void testCheckAnswerChoosingOutOfRangeOption() {
        List<Option> options = List.of(
                new Option("False", false),
                new Option("True", true)
        );
        Question question = new Question("Is testing fun?", options,
                OppositionTopic.TAI_ALL);

        boolean result = service.checkAnswer(question, 99);
        assertFalse(result, "It should have returned false for out of range option");
    }

    @Test
    @DisplayName("Should return 1 question")
    void testGetQuestionCount() {
        int count = service.getQuestionCount(OppositionTopic.ADVO_ALL);
        assertEquals(1, count);
    }
}
