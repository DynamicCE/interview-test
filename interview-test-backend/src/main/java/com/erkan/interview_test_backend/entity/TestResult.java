package com.erkan.interview_test_backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer score;

    @ElementCollection
    @CollectionTable(name = "weak_topics", joinColumns = @JoinColumn(name = "test_result_id"))
    @Column(name = "topic")
    private List<String> weakTopics;

    private LocalDateTime testDate;

    @Enumerated(EnumType.STRING)
    private TestCategory category;

    public TestResult() {}

    public TestResult(Long id, Integer score, List<String> weakTopics, LocalDateTime testDate,
            TestCategory category) {
        this.id = id;
        this.score = score;
        this.weakTopics = weakTopics;
        this.testDate = testDate;
        this.category = category;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public List<String> getWeakTopics() {
        return weakTopics;
    }

    public LocalDateTime getTestDate() {
        return testDate;
    }

    public TestCategory getCategory() {
        return category;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setWeakTopics(List<String> weakTopics) {
        this.weakTopics = weakTopics;
    }

    public void setTestDate(LocalDateTime testDate) {
        this.testDate = testDate;
    }

    public void setCategory(TestCategory category) {
        this.category = category;
    }

    // Builder
    public static TestResultBuilder builder() {
        return new TestResultBuilder();
    }

    public static class TestResultBuilder {
        private Long id;
        private Integer score;
        private List<String> weakTopics;
        private LocalDateTime testDate;
        private TestCategory category;

        public TestResultBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TestResultBuilder score(Integer score) {
            this.score = score;
            return this;
        }

        public TestResultBuilder weakTopics(List<String> weakTopics) {
            this.weakTopics = weakTopics;
            return this;
        }

        public TestResultBuilder testDate(LocalDateTime testDate) {
            this.testDate = testDate;
            return this;
        }

        public TestResultBuilder category(TestCategory category) {
            this.category = category;
            return this;
        }

        public TestResult build() {
            return new TestResult(id, score, weakTopics, testDate, category);
        }
    }
}
