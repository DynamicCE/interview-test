package com.erkan.interview_test_backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.erkan.interview_test_backend.dto.HuggingFaceRequestDTO;
import com.erkan.interview_test_backend.dto.HuggingFaceResponseDTO;
import com.erkan.interview_test_backend.dto.QuestionDTO;
import com.erkan.interview_test_backend.entity.TestCategory;
import com.erkan.interview_test_backend.exception.ExternalApiException;

@Service
public class HuggingFaceService {

    @Value("${huggingface.api.url}")
    private String apiUrl;

    @Value("${huggingface.api.token}")
    private String apiToken;

    private final RestTemplate restTemplate;

    public HuggingFaceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<QuestionDTO> generateQuestions(TestCategory category, int count) {
        List<QuestionDTO> questions = new ArrayList<>();
        try {
            for (int i = 0; i < count; i++) {
                questions.add(generateSingleQuestion(category));
            }
            return questions;
        } catch (Exception e) {
            if (e instanceof ExternalApiException) {
                throw e;
            }
            throw new ExternalApiException(
                    "HuggingFace API çağrısı sırasında hata oluştu: " + e.getMessage());
        }
    }

    private QuestionDTO generateSingleQuestion(TestCategory category) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);

            HuggingFaceRequestDTO request = createRequest(category);
            HttpEntity<HuggingFaceRequestDTO> entity = new HttpEntity<>(request, headers);

            HuggingFaceResponseDTO response =
                    restTemplate.postForObject(apiUrl, entity, HuggingFaceResponseDTO.class);

            if (response == null || response.getGeneratedText() == null) {
                throw new ExternalApiException(
                        "HuggingFace API çağrısı sırasında hata oluştu: HuggingFace API null yanıt döndü");
            }

            String generatedText = response.getGeneratedText();
            if (generatedText.trim().isEmpty()) {
                throw new ExternalApiException(
                        "HuggingFace API çağrısı sırasında hata oluştu: Yanıt metni boş");
            }

            return parseResponse(generatedText);
        } catch (Exception e) {
            // Herhangi bir hata durumunda mock data dön
            return generateMockQuestion(category, 0);
        }
    }

    private HuggingFaceRequestDTO createRequest(TestCategory category) {
        String prompt = String.format("Java %s konusunda bir mülakat sorusu üret. "
                + "4 şıklı çoktan seçmeli olsun. " + "Doğru cevabı ve açıklamasını da ekle.",
                category.toString());

        HuggingFaceRequestDTO.Parameters params = HuggingFaceRequestDTO.Parameters.builder()
                .maxLength(500).temperature(0.7).topK(50).topP(0.9).build();

        return HuggingFaceRequestDTO.builder().prompt(prompt).parameters(params).build();
    }

    private QuestionDTO parseResponse(String generatedText) {
        try {
            String[] lines = generatedText.split("\n");
            if (lines.length < 7) {
                throw new IllegalArgumentException("Yetersiz satır sayısı: " + lines.length);
            }

            String question = lines[0].trim();
            List<String> options = Arrays.asList(lines[1].trim(), lines[2].trim(), lines[3].trim(),
                    lines[4].trim());
            String correctAnswer = lines[5].trim();
            String explanation = lines[6].trim();

            if (question.isEmpty() || options.stream().anyMatch(String::isEmpty)
                    || correctAnswer.isEmpty() || explanation.isEmpty()) {
                throw new IllegalArgumentException("Boş satır(lar) mevcut");
            }

            return QuestionDTO.builder().question(question).options(options)
                    .correctAnswer(correctAnswer).explanation(explanation).build();
        } catch (Exception e) {
            // Herhangi bir hata durumunda mock data dön
            return generateMockQuestion(TestCategory.CORE_JAVA, 0);
        }
    }

    private QuestionDTO generateMockQuestion(TestCategory category, int index) {
        return QuestionDTO.builder().question(category + " kategorisi için örnek soru " + index)
                .options(List.of("Seçenek A", "Seçenek B", "Seçenek C", "Seçenek D"))
                .correctAnswer("Seçenek A")
                .explanation("Doğru cevabın açıklaması burada yer alacak").build();
    }
}
