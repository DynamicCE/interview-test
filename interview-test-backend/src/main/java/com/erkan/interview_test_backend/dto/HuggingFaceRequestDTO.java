package com.erkan.interview_test_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HuggingFaceRequestDTO {
    private String inputs;
    private Parameters parameters;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameters {
        private Integer maxLength;
        private Double temperature;
        private Integer topK;
        private Double topP;
    }
}
