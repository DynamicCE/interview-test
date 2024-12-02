package com.erkan.interview_test_backend.dto;

import java.util.List;

public class QuestionDTO {
    private String question;
    private List<String> options;
    private String correctAnswer;
    private String explanation;

    public QuestionDTO() {}

    public QuestionDTO(String question, List<String> options, String correctAnswer,
            String explanation) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    // Getters
    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getExplanation() {
        return explanation;
    }

    // Setters
    public void setQuestion(String question) {
        this.question = question;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    // Builder
    public static QuestionDTOBuilder builder() {
        return new QuestionDTOBuilder();
    }

    public static class QuestionDTOBuilder {
        private String question;
        private List<String> options;
        private String correctAnswer;
        private String explanation;

        public QuestionDTOBuilder question(String question) {
            this.question = question;
            return this;
        }

        public QuestionDTOBuilder options(List<String> options) {
            this.options = options;
            return this;
        }

        public QuestionDTOBuilder correctAnswer(String correctAnswer) {
            this.correctAnswer = correctAnswer;
            return this;
        }

        public QuestionDTOBuilder explanation(String explanation) {
            this.explanation = explanation;
            return this;
        }

        public QuestionDTO build() {
            return new QuestionDTO(question, options, correctAnswer, explanation);
        }
    }
}
