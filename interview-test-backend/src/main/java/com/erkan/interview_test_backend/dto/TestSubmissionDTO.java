package com.erkan.interview_test_backend.dto;

import java.util.List;

import com.erkan.interview_test_backend.entity.TestCategory;

public class TestSubmissionDTO {
    private String testId;
    private TestCategory category;
    private List<String> answers;
    private List<String> weakTopics;

    public TestSubmissionDTO() {}

    public TestSubmissionDTO(String testId, TestCategory category, List<String> answers,
            List<String> weakTopics) {
        this.testId = testId;
        this.category = category;
        this.answers = answers;
        this.weakTopics = weakTopics;
    }

    // Getters
    public String getTestId() {
        return testId;
    }

    public TestCategory getCategory() {
        return category;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<String> getWeakTopics() {
        return weakTopics;
    }

    // Setters
    public void setTestId(String testId) {
        this.testId = testId;
    }

    public void setCategory(TestCategory category) {
        this.category = category;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setWeakTopics(List<String> weakTopics) {
        this.weakTopics = weakTopics;
    }

    // Builder
    public static TestSubmissionDTOBuilder builder() {
        return new TestSubmissionDTOBuilder();
    }

    public static class TestSubmissionDTOBuilder {
        private String testId;
        private TestCategory category;
        private List<String> answers;
        private List<String> weakTopics;

        public TestSubmissionDTOBuilder testId(String testId) {
            this.testId = testId;
            return this;
        }

        public TestSubmissionDTOBuilder category(TestCategory category) {
            this.category = category;
            return this;
        }

        public TestSubmissionDTOBuilder answers(List<String> answers) {
            this.answers = answers;
            return this;
        }

        public TestSubmissionDTOBuilder weakTopics(List<String> weakTopics) {
            this.weakTopics = weakTopics;
            return this;
        }

        public TestSubmissionDTO build() {
            return new TestSubmissionDTO(testId, category, answers, weakTopics);
        }
    }
}
