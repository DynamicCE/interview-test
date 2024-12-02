package com.erkan.interview_test_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.dto.TestSubmissionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.service.TestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Test Controller", description = "Java mülakat testi için API endpointleri")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "Yeni test başlat",
            description = "Seçilen kategori için 10 soruluk test oluşturur")
    @GetMapping("/start/{category}")
    public ResponseEntity<List<QuestionDTO>> startTest(@PathVariable TestCategory category) {
        return ResponseEntity.ok(testService.startTest(category));
    }

    @Operation(summary = "Test sonuçlarını kaydet",
            description = "Kullanıcının cevaplarını değerlendirir ve sonucu kaydeder")
    @PostMapping("/submit")
    public ResponseEntity<TestResult> submitTest(@RequestBody TestSubmissionDTO submission) {
        return ResponseEntity.ok(testService.submitTest(submission.getTestId(),
                submission.getCategory(), submission.getAnswers(), submission.getWeakTopics()));
    }
}
