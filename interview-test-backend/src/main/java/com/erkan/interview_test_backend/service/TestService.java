package com.erkan.interview_test_backend.service;

import org.springframework.stereotype.Service;
import com.erkan.interview_test_backend.dto.TestResponseDTO;
import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import com.erkan.interview_test_backend.dto.TestResultDTO;
import com.erkan.interview_test_backend.dto.TestSubmissionDTO;
import com.erkan.interview_test_backend.service.OpenAIService;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestService {

    private final OpenAIService openAIService;
    private final Map<String, List<QuestionDTO>> activeTests = new HashMap<>();

    public TestResponseDTO startTest(TestCategory category) {
        log.info("Generating test for category: {}", category);

        // Test ID oluştur
        String testId = UUID.randomUUID().toString();

        // Soruları al (her kategori için 3 soru)
        List<QuestionDTO> questions = openAIService.generateQuestions(category, 3);

        // Aktif teste ekle
        activeTests.put(testId, questions);

        // Response oluştur
        TestResponseDTO response = new TestResponseDTO();
        response.setTestId(testId);
        response.setQuestions(questions);

        return response;
    }

    public TestResultDTO evaluateTest(TestSubmissionDTO submission) {
        List<QuestionDTO> questions = activeTests.get(submission.getTestId());
        if (questions == null) {
            throw new RuntimeException("Test bulunamadı");
        }

        // Doğru cevapları say
        int correct = 0;
        List<String> weakTopics = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            QuestionDTO question = questions.get(i);
            String userAnswer = submission.getAnswers().get(i);

            if (!question.getCorrectAnswer().equals(userAnswer)) {
                // Yanlış cevaplanan konuları ekle
                weakTopics.add(question.getQuestion().split(" ")[0]); // İlk kelimeyi konu olarak al
            } else {
                correct++;
            }
        }

        // Sonucu hazırla
        TestResultDTO result = new TestResultDTO();
        result.setScore((correct * 100) / questions.size());
        result.setWeakTopics(weakTopics);

        // Geri bildirim oluştur
        String feedback = String.format(
                "Toplam %d sorudan %d tanesini doğru cevapladınız. %s",
                questions.size(),
                correct,
                weakTopics.isEmpty() ? "Harika bir performans!"
                        : "Şu konulara daha fazla çalışmalısınız: " + String.join(", ", weakTopics));
        result.setFeedback(feedback);

        // Test bittikten sonra temizle
        activeTests.remove(submission.getTestId());

        return result;
    }
}
