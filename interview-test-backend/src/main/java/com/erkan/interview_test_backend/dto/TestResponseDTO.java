package com.erkan.interview_test_backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class TestResponseDTO {
    private String testId;
    private List<QuestionDTO> questions;
}