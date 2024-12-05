package com.erkan.interview_test_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestResultDTO {
    private Integer score;
    private List<String> weakTopics;
    private String feedback;
}