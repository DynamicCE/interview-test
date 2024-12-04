package com.erkan.interview_test_backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erkan.interview_test_backend.entity.TestResult;
import com.erkan.interview_test_backend.repository.TestResultRepository;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository testResultRepository;

    public TestResult save(TestResult testResult) {
        return testResultRepository.save(testResult);
    }

    public TestResult findById(Long id) {
        return testResultRepository.findById(id).orElse(null);
    }
}
