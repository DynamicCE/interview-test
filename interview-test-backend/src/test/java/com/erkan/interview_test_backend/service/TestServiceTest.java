package com.erkan.interview_test_backend.service;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.repository.TestResultRepository;

class TestServiceTest {

    @Mock
    private HuggingFaceService huggingFaceService;

    @Mock
    private TestResultRepository testResultRepository;

    private TestService testService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this).close();
        testService = new TestService(huggingFaceService, testResultRepository);
    }

    @Test
    void startTest_ShouldReturnQuestions() {
        // Given
        List<QuestionDTO> mockQuestions =
                Arrays.asList(createMockQuestion("Soru 1"), createMockQuestion("Soru 2"));
        when(huggingFaceService.generateQuestions(any(), any(Integer.class)))
                .thenReturn(mockQuestions);

        // When
        List<QuestionDTO> result = testService.startTest(TestCategory.CORE_JAVA);

        // Then
        assertEquals(2, result.size());
        assertEquals("Soru 1", result.get(0).getQuestion());
    }

    @Test
    void submitTest_WithValidAnswers_ShouldCalculateScore() {
        // Given
        String testId = "test-123";
        List<String> answers = Arrays.asList("Seçenek A", "Seçenek B");
        List<String> weakTopics = Arrays.asList("Threads", "Collections");

        // Test öncesi active tests'e mock soruları ekle
        List<QuestionDTO> mockQuestions = Arrays.asList(createMockQuestion("Soru 1", "Seçenek A"),
                createMockQuestion("Soru 2", "Seçenek A"));
        testService.getActiveTests().put(testId, mockQuestions);

        when(testResultRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        // When
        TestResult result =
                testService.submitTest(testId, TestCategory.CORE_JAVA, answers, weakTopics);

        // Then
        assertEquals(50, result.getScore());
        assertEquals(weakTopics, result.getWeakTopics());
    }

    private QuestionDTO createMockQuestion(String question) {
        return createMockQuestion(question, "Seçenek A");
    }

    private QuestionDTO createMockQuestion(String question, String correctAnswer) {
        QuestionDTO dto = new QuestionDTO();
        dto.setQuestion(question);
        dto.setOptions(Arrays.asList("Seçenek A", "Seçenek B", "Seçenek C", "Seçenek D"));
        dto.setCorrectAnswer(correctAnswer);
        dto.setExplanation("Açıklama");
        return dto;
    }
}
