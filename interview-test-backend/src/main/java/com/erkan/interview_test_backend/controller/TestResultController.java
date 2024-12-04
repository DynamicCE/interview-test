package com.erkan.interview_test_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.service.TestResultService;

@RestController
@RequestMapping("/api/test-results")
public class TestResultController {

    @Autowired
    private TestResultService testResultService;

    @PostMapping
    public ResponseEntity<TestResult> saveTestResult(@RequestBody TestResult testResult) {
        TestResult savedResult = testResultService.save(testResult);
        return ResponseEntity.ok(savedResult);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResult> getTestResult(@PathVariable Long id) {
        TestResult result = testResultService.findById(id);
        return ResponseEntity.ok(result);
    }
}
