package com.erkan.interview_test_backend.service;

import java.util.ArrayList;
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
        try {
            List<QuestionDTO> questions = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                questions.add(generateSingleQuestion(category));
            }
            return questions;
        } catch (Exception e) {
            throw new ExternalApiException(
                    "HuggingFace API çağrısı sırasında hata oluştu: " + e.getMessage());
        }
    }

    private QuestionDTO generateSingleQuestion(TestCategory category) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        HuggingFaceRequestDTO request = createRequest(category);
        HttpEntity<HuggingFaceRequestDTO> entity = new HttpEntity<>(request, headers);

        HuggingFaceResponseDTO response =
                restTemplate.postForObject(apiUrl, entity, HuggingFaceResponseDTO.class);

        return parseResponse(response.getGeneratedText());
    }

    private HuggingFaceRequestDTO createRequest(TestCategory category) {
        String prompt = String.format("Java %s konusunda bir mülakat sorusu üret. "
                + "4 şıklı çoktan seçmeli olsun. " + "Doğru cevabı ve açıklamasını da ekle.",
                category.toString());

        HuggingFaceRequestDTO.Parameters params = new HuggingFaceRequestDTO.Parameters(500, // maxLength
                0.7, // temperature
                50, // topK
                0.9 // topP
        );

        return new HuggingFaceRequestDTO(prompt, params);
    }

    private QuestionDTO parseResponse(String generatedText) {
        // TODO: AI'dan gelen metni parse edip QuestionDTO'ya çevir
        // Şimdilik mock data dönüyoruz
        return generateMockQuestion(TestCategory.CORE_JAVA, 0);
    }

    private QuestionDTO generateMockQuestion(TestCategory category, int index) {
        QuestionDTO question = new QuestionDTO();
        question.setQuestion(category + " kategorisi için örnek soru " + index);
        question.setOptions(List.of("Seçenek A", "Seçenek B", "Seçenek C", "Seçenek D"));
        question.setCorrectAnswer("Seçenek A");
        question.setExplanation("Doğru cevabın açıklaması burada yer alacak");
        return question;
    }
}
