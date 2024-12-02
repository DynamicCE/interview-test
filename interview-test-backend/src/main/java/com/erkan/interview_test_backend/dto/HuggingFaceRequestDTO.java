package com.erkan.interview_test_backend.dto;

public class HuggingFaceRequestDTO {
    private String prompt;
    private Parameters parameters;

    public HuggingFaceRequestDTO() {}

    public HuggingFaceRequestDTO(String prompt, Parameters parameters) {
        this.prompt = prompt;
        this.parameters = parameters;
    }

    // Getters
    public String getPrompt() {
        return prompt;
    }

    public Parameters getParameters() {
        return parameters;
    }

    // Setters
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    // Builder
    public static HuggingFaceRequestDTOBuilder builder() {
        return new HuggingFaceRequestDTOBuilder();
    }

    public static class Parameters {
        private Integer maxLength;
        private Double temperature;
        private Integer topK;
        private Double topP;

        public Parameters() {}

        public Parameters(Integer maxLength, Double temperature, Integer topK, Double topP) {
            this.maxLength = maxLength;
            this.temperature = temperature;
            this.topK = topK;
            this.topP = topP;
        }

        // Getters
        public Integer getMaxLength() {
            return maxLength;
        }

        public Double getTemperature() {
            return temperature;
        }

        public Integer getTopK() {
            return topK;
        }

        public Double getTopP() {
            return topP;
        }

        // Setters
        public void setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public void setTopK(Integer topK) {
            this.topK = topK;
        }

        public void setTopP(Double topP) {
            this.topP = topP;
        }

        // Builder
        public static ParametersBuilder builder() {
            return new ParametersBuilder();
        }

        public static class ParametersBuilder {
            private Integer maxLength;
            private Double temperature;
            private Integer topK;
            private Double topP;

            public ParametersBuilder maxLength(Integer maxLength) {
                this.maxLength = maxLength;
                return this;
            }

            public ParametersBuilder temperature(Double temperature) {
                this.temperature = temperature;
                return this;
            }

            public ParametersBuilder topK(Integer topK) {
                this.topK = topK;
                return this;
            }

            public ParametersBuilder topP(Double topP) {
                this.topP = topP;
                return this;
            }

            public Parameters build() {
                return new Parameters(maxLength, temperature, topK, topP);
            }
        }
    }

    public static class HuggingFaceRequestDTOBuilder {
        private String prompt;
        private Parameters parameters;

        public HuggingFaceRequestDTOBuilder prompt(String prompt) {
            this.prompt = prompt;
            return this;
        }

        public HuggingFaceRequestDTOBuilder parameters(Parameters parameters) {
            this.parameters = parameters;
            return this;
        }

        public HuggingFaceRequestDTO build() {
            return new HuggingFaceRequestDTO(prompt, parameters);
        }
    }
}
