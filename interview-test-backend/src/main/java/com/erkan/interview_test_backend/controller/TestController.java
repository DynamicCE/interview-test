package com.erkan.interview_test_backend.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.dto.TestSubmissionDTO;
import com.erkan.interview_test_backend.dto.TestResultDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.service.TestService;
import com.erkan.interview_test_backend.service.OpenAIService;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class TestController {

    private final TestService testService;
    private final OpenAIService openAIService;

    @GetMapping("/start/{category}")
    public ResponseEntity<List<QuestionDTO>> startTest(@PathVariable TestCategory category) {
        log.debug("Test başlatma isteği alındı. Kategori: {}", category);
        return ResponseEntity.ok(openAIService.generateQuestions(category, 3));
    }

    @PostMapping("/submit")
    public ResponseEntity<TestResultDTO> submitTest(@RequestBody TestSubmissionDTO submission) {
        log.debug("Test submission received: {}", submission);
        TestResultDTO result = testService.evaluateTest(submission);
        return ResponseEntity.ok(result);
    }
}
