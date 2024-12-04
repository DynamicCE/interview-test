package com.erkan.interview_test_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startTest_ShouldReturnQuestions() {
        // Given
        List<QuestionDTO> mockQuestions = Arrays.asList(
            QuestionDTO.builder()
                .question("What is Java?")
                .options(Arrays.asList("A", "B", "C", "D"))
                .correctAnswer("A")
                .explanation("Java is a programming language")
                .build()
        );

        when(huggingFaceService.generateQuestions(any(TestCategory.class), any(Integer.class)))
            .thenReturn(mockQuestions);

        // When
        List<QuestionDTO> result = testService.startTest(TestCategory.CORE_JAVA);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("What is Java?", result.get(0).getQuestion());
    }

    @Test
    void submitTest_ShouldCalculateScoreCorrectly() {
        // Given
        String testId = testService.getCurrentTestId();
        List<QuestionDTO> questions = Arrays.asList(
            QuestionDTO.builder()
                .question("What is Java?")
                .options(Arrays.asList("A", "B", "C", "D"))
                .correctAnswer("A")
                .explanation("Java is a programming language")
                .build()
        );
        testService.getActiveTests().put(testId, questions);

        List<String> answers = Arrays.asList("A");
        List<String> weakTopics = Arrays.asList("OOP");

        when(testResultRepository.save(any(TestResult.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TestResult result = testService.submitTest(testId, TestCategory.CORE_JAVA, answers, weakTopics);

        // Then
        assertNotNull(result);
        assertEquals(100, result.getScore());
        assertEquals(TestCategory.CORE_JAVA, result.getCategory());
        assertEquals(weakTopics, result.getWeakTopics());
    }
}
