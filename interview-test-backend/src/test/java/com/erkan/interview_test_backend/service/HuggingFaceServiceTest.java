package com.erkan.interview_test_backend.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.erkan.interview_test_backend.dto.HuggingFaceResponseDTO;
import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;

@ExtendWith(MockitoExtension.class)
class HuggingFaceServiceTest {

    private static final String API_URL = "http://test-api.com";
    private static final String API_TOKEN = "test-token";

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private HuggingFaceService huggingFaceService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(huggingFaceService, "apiUrl", API_URL);
        ReflectionTestUtils.setField(huggingFaceService, "apiToken", API_TOKEN);
    }

    @Test
    void generateQuestions_ShouldReturnQuestionList() {
        // Given
        HuggingFaceResponseDTO mockResponse = new HuggingFaceResponseDTO();
        mockResponse.setGeneratedText("""
                Java nedir?
                A) Bir programlama dili
                B) Bir veritabanı
                C) Bir işletim sistemi
                D) Bir web tarayıcısı
                A
                Java, Sun Microsystems tarafından geliştirilmiş bir programlama dilidir.""");

        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class),
                eq(HuggingFaceResponseDTO.class))).thenReturn(mockResponse);

        // When
        List<QuestionDTO> questions =
                huggingFaceService.generateQuestions(TestCategory.CORE_JAVA, 1);

        // Then
        assertNotNull(questions);
        assertEquals(1, questions.size());

        QuestionDTO question = questions.get(0);
        assertEquals("Java nedir?", question.getQuestion());
        assertEquals(4, question.getOptions().size());
        assertEquals("A", question.getCorrectAnswer());
    }

    @Test
    void generateQuestions_WhenApiFailsShouldReturnMockData() {
        // Given
        when(restTemplate.postForObject(eq(API_URL), any(HttpEntity.class),
                eq(HuggingFaceResponseDTO.class))).thenReturn(null);

        // When
        List<QuestionDTO> questions =
                huggingFaceService.generateQuestions(TestCategory.CORE_JAVA, 1);

        // Then
        assertNotNull(questions);
        assertEquals(1, questions.size());
        assertTrue(questions.get(0).getQuestion().contains("CORE_JAVA"));
    }
}
