package com.erkan.interview_test_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;

class HuggingFaceServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HuggingFaceService huggingFaceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateQuestions_ShouldReturnQuestionList() {
        // Given
        String mockResponse = "Here are some Java questions:\n" +
                "1. What is inheritance in Java?\n" +
                "A) A mechanism to reuse code\n" +
                "B) A way to create objects\n" +
                "C) A type of variable\n" +
                "D) A method declaration\n" +
                "Correct Answer: A\n" +
                "Explanation: Inheritance is a mechanism that allows a class to inherit properties and methods from another class.";

        when(restTemplate.postForObject(any(String.class), any(), any()))
                .thenReturn(mockResponse);

        // When
        List<QuestionDTO> questions = huggingFaceService.generateQuestions(TestCategory.CORE_JAVA, 3);

        // Then
        assertNotNull(questions);
        assertEquals(3, questions.size());
    }

    @Test
    void generateQuestions_WhenApiFailsShouldReturnMockData() {
        // Given
        when(restTemplate.postForObject(any(String.class), any(), any()))
                .thenThrow(new RuntimeException("API Error"));

        // When
        List<QuestionDTO> questions = huggingFaceService.generateQuestions(TestCategory.CORE_JAVA, 3);

        // Then
        assertNotNull(questions);
        assertEquals(3, questions.size());
    }
}
