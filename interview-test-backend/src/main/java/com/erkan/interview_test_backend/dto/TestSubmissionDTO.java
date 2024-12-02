package com.erkan.interview_test_backend.dto;

import java.util.List;

import com.erkan.interview_test_backend.entity.TestCategory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSubmissionDTO {
    private String testId;
    private TestCategory category;
    private List<String> answers;
    private List<String> weakTopics;
}
