package com.erkan.interview_test_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.dto.TestSubmissionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.service.TestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@Tag(name = "Test Controller", description = "Java mülakat testi için API endpointleri")
@Slf4j
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @Operation(summary = "Yeni bir test başlat",
            description = "Seçilen kategoride yeni bir test başlatır ve soruları döner")
    @GetMapping("/start/{category}")
    public ResponseEntity<?> startTest(@PathVariable TestCategory category) {
        try {
            log.debug("Test başlatma isteği alındı. Kategori: {}", category);
            List<QuestionDTO> questions = testService.startTest(category);
            
            Map<String, Object> response = new HashMap<>();
            response.put("testId", testService.getCurrentTestId());
            response.put("questions", questions);
            
            log.debug("Test başlatıldı. Response: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Test başlatılırken hata oluştu: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Test başlatılırken bir hata oluştu", e.getMessage()));
        }
    }

    @Operation(summary = "Test sonuçlarını kaydet",
            description = "Kullanıcının cevaplarını değerlendirir ve sonucu kaydeder")
    @PostMapping("/submit")
    public ResponseEntity<?> submitTest(@RequestBody TestSubmissionDTO submission) {
        try {
            log.debug("Test sonucu gönderildi: {}", submission);
            TestResult result = testService.submitTest(
                submission.getTestId(),
                submission.getCategory(),
                submission.getAnswers(),
                submission.getWeakTopics()
            );
            log.debug("Test sonucu kaydedildi: {}", result);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("Test sonuçları kaydedilirken hata oluştu: ", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Test sonuçları kaydedilirken bir hata oluştu", e.getMessage()));
        }
    }
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private String message;
    private String details;
}
