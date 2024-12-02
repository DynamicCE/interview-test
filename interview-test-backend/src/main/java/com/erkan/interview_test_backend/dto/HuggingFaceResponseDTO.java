package com.erkan.interview_test_backend.dto;

public class HuggingFaceResponseDTO {
    private String generatedText;

    public HuggingFaceResponseDTO() {}

    public HuggingFaceResponseDTO(String generatedText) {
        this.generatedText = generatedText;
    }

    // Getters
    public String getGeneratedText() {
        return generatedText;
    }

    // Setters
    public void setGeneratedText(String generatedText) {
        this.generatedText = generatedText;
    }

    // Builder
    public static HuggingFaceResponseDTOBuilder builder() {
        return new HuggingFaceResponseDTOBuilder();
    }

    public static class HuggingFaceResponseDTOBuilder {
        private String generatedText;

        public HuggingFaceResponseDTOBuilder generatedText(String generatedText) {
            this.generatedText = generatedText;
            return this;
        }

        public HuggingFaceResponseDTO build() {
            return new HuggingFaceResponseDTO(generatedText);
        }
    }
}
