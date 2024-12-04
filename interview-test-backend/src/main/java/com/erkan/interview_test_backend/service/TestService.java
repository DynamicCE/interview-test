package com.erkan.interview_test_backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.repository.TestResultRepository;

@Service
public class TestService {

    private static final Logger log = LoggerFactory.getLogger(TestService.class);

    private final HuggingFaceService huggingFaceService;
    private final TestResultRepository testResultRepository;
    private final Map<String, List<QuestionDTO>> activeTests;
    private String currentTestId;

    public TestService(HuggingFaceService huggingFaceService,
            TestResultRepository testResultRepository) {
        this.huggingFaceService = huggingFaceService;
        this.testResultRepository = testResultRepository;
        this.activeTests = new ConcurrentHashMap<>();
    }

    public List<QuestionDTO> startTest(TestCategory category) {
        try {
            log.debug("Test başlatılıyor. Kategori: {}", category);
            List<QuestionDTO> questions = huggingFaceService.generateQuestions(category, 3);
            currentTestId = generateTestId();
            log.debug("Test ID oluşturuldu: {}", currentTestId);
            activeTests.put(currentTestId, questions);
            log.debug("Sorular hazırlandı: {}", questions);
            return questions;
        } catch (Exception e) {
            log.error("Test başlatılırken hata oluştu", e);
            throw e;
        }
    }

    public String getCurrentTestId() {
        return currentTestId;
    }

    public TestResult submitTest(String testId, TestCategory category, List<String> answers,
            List<String> weakTopics) {
        List<QuestionDTO> questions = activeTests.get(testId);
        if (questions == null) {
            throw new IllegalStateException("Test bulunamadı veya süresi dolmuş");
        }

        TestResult result =
                TestResult.builder().category(category).score(calculateScore(questions, answers))
                        .weakTopics(weakTopics).testDate(LocalDateTime.now()).build();

        activeTests.remove(testId);
        return testResultRepository.save(result);
    }

    private Integer calculateScore(List<QuestionDTO> questions, List<String> answers) {
        if (questions.size() != answers.size()) {
            throw new IllegalArgumentException("Cevap sayısı soru sayısı ile eşleşmiyor");
        }

        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            log.debug("Soru {}: Beklenen cevap = {}, Verilen cevap = {}", 
                i + 1, questions.get(i).getCorrectAnswer(), answers.get(i));
            if (questions.get(i).getCorrectAnswer().equals(answers.get(i))) {
                correctAnswers++;
            }
        }

        int score = (correctAnswers * 100) / questions.size();
        log.debug("Toplam soru: {}, Doğru cevap: {}, Puan: {}", 
            questions.size(), correctAnswers, score);
        return score;
    }

    private String generateTestId() {
        return System.currentTimeMillis() + "-" + Math.random();
    }

    // Sadece test için
    Map<String, List<QuestionDTO>> getActiveTests() {
        return activeTests;
    }
}
