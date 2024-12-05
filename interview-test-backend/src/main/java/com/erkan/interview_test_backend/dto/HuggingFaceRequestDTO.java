package com.erkan.interview_test_backend.dto;

import java.util.Map;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HuggingFaceRequestDTO {
    private String inputs;
    private Map<String, Object> parameters;
}
