package com.erkan.interview_test_backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class OpenAIService {

    @Qualifier("openAIRestTemplate")
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.ai.openai.base-url}${spring.ai.openai.chat.completions-path}")
    private String apiUrl;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    public List<QuestionDTO> generateQuestions(TestCategory category, int count) {
        log.debug("Generating {} questions for category: {}", count, category);
        List<QuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            String prompt = generatePrompt(category);
            String response = callOpenAIApi(prompt);
            QuestionDTO question = parseResponse(response);
            questions.add(question);
        }

        return questions;
    }

    private String generatePrompt(TestCategory category) {
        return String.format(
                """
                        Generate a technical multiple choice question about %s.
                        The response must be in exactly this format:
                        Question: <write a technical question>
                        Options:
                        A) <write first option>
                        B) <write second option>
                        C) <write third option>
                        D) <write fourth option>
                        Correct Answer: <write A, B, C or D>
                        Explanation: <write a detailed explanation>
                        """,
                category.toString().replace("_", " ").toLowerCase());
    }

    private String callOpenAIApi(String prompt) {
        try {
            Map<String, Object> message = Map.of(
                    "role", "user",
                    "content", prompt);

            Map<String, Object> requestBody = Map.of(
                    "model", "gpt-3.5-turbo",
                    "messages", Collections.singletonList(message),
                    "temperature", 0.7,
                    "max_tokens", 1000);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            log.info("Full Request URL: {}", apiUrl);
            log.info("Full Request Body: {}", objectMapper.writeValueAsString(requestBody));

            JsonNode response = restTemplate.postForObject(apiUrl, entity, JsonNode.class);

            if (response == null) {
                throw new RuntimeException("OpenAI API yanıt vermedi");
            }

            log.info("Full Response: {}", objectMapper.writeValueAsString(response));
            return response.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            log.error("OpenAI API çağrısı sırasında hata detayı: ", e);
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException httpError = (HttpClientErrorException) e;
                log.error("HTTP Status: {}", httpError.getStatusCode());
                log.error("Response Body: {}", httpError.getResponseBodyAsString());
                log.error("Response Headers: {}", httpError.getResponseHeaders());
            }
            throw new RuntimeException("Soru üretilirken bir hata oluştu", e);
        }
    }

    private QuestionDTO parseResponse(String response) {
        try {
            log.debug("Parsing response: {}", response);

            Pattern pattern = Pattern.compile(
                    "Question:\\s*(.+?)\\s*Options:\\s*A\\)\\s*(.+?)\\s*B\\)\\s*(.+?)\\s*C\\)\\s*(.+?)\\s*D\\)\\s*(.+?)\\s*Correct Answer:\\s*([ABCD])\\s*Explanation:\\s*(.+)",
                    Pattern.DOTALL);

            Matcher matcher = pattern.matcher(response);

            if (matcher.find()) {
                QuestionDTO question = new QuestionDTO();
                question.setQuestion(matcher.group(1).trim());
                question.setOptions(Arrays.asList(
                        matcher.group(2).trim(),
                        matcher.group(3).trim(),
                        matcher.group(4).trim(),
                        matcher.group(5).trim()));
                question.setCorrectAnswer(matcher.group(6).trim());
                question.setExplanation(matcher.group(7).trim());
                return question;
            }

            log.error("API yanıtı parse edilemedi: {}", response);
            throw new RuntimeException("API yanıtı beklenmeyen formatta");
        } catch (Exception e) {
            log.error("API yanıtı parse edilirken hata: ", e);
            throw new RuntimeException("Soru formatı işlenirken bir hata oluştu", e);
        }
    }
}