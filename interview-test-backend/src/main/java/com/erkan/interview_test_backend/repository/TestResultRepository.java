package com.erkan.interview_test_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.entity.TestResult;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByCategory(TestCategory category);

    List<TestResult> findTop10ByOrderByTestDateDesc();
}
