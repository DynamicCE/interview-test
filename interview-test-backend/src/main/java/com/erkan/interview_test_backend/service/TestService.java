package com.erkan.interview_test_backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.repository.TestResultRepository;

@Service
public class TestService {

    private final HuggingFaceService huggingFaceService;
    private final TestResultRepository testResultRepository;

    public TestService(HuggingFaceService huggingFaceService,
            TestResultRepository testResultRepository) {
        this.huggingFaceService = huggingFaceService;
        this.testResultRepository = testResultRepository;
    }

    public List<QuestionDTO> startTest(TestCategory category) {
        return huggingFaceService.generateQuestions(category, 10);
    }

    public TestResult submitTest(TestCategory category, List<String> answers,
            List<String> weakTopics) {
        TestResult result = new TestResult();
        result.setCategory(category);
        result.setScore(calculateScore(answers)); // TODO: Gerçek puanlama sistemi eklenecek
        result.setWeakTopics(weakTopics);
        result.setTestDate(LocalDateTime.now());

        return testResultRepository.save(result);
    }

    private Integer calculateScore(List<String> answers) {
        // TODO: Gerçek puanlama mantığı implement edilecek
        return 70; // Şimdilik örnek puan
    }
}
