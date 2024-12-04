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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
            switch (category) {
                case CORE_JAVA:
                    questions.add(generateMockQuestion(category, 0)); // final keyword sorusu
                    questions.add(createJavaQuestion("Java'da 'static' keyword'ü ne işe yarar?",
                        List.of("Sadece metod tanımlamak için kullanılır",
                            "Sınıf seviyesinde paylaşılan öğeler tanımlamak için kullanılır",
                            "Sadece değişken tanımlamak için kullanılır",
                            "Sadece blok tanımlamak için kullanılır"),
                        "B",
                        "static keyword'ü ile tanımlanan öğeler sınıf seviyesinde paylaşılır ve nesne oluşturmadan erişilebilir."));
                    questions.add(createJavaQuestion("Java'da 'interface' ve 'abstract class' arasındaki fark nedir?",
                        List.of("Interface'ler çoklu kalıtımı destekler, abstract class'lar desteklemez",
                            "Abstract class'lar constructor içerebilir, interface'ler içeremez",
                            "Interface'ler sadece metod imzası içerebilir",
                            "Yukarıdakilerin hepsi"),
                        "D",
                        "Interface'ler çoklu kalıtımı destekler, sadece metod imzası içerebilir (Java 8'den önce). Abstract class'lar constructor içerebilir ve tek kalıtımı destekler."));
                    break;

                case SPRING_FRAMEWORK:
                    questions.add(generateMockQuestion(category, 0)); // @Autowired sorusu
                    questions.add(createJavaQuestion("Spring Boot'ta @RestController ve @Controller arasındaki fark nedir?",
                        List.of("@RestController sadece REST API'ler için kullanılır",
                            "@Controller sadece MVC uygulamaları için kullanılır",
                            "@RestController = @Controller + @ResponseBody",
                            "Hiçbir fark yoktur"),
                        "C",
                        "@RestController, @Controller ve @ResponseBody anotasyonlarının birleşimidir. REST API'ler için otomatik olarak response'ları JSON/XML formatına çevirir."));
                    questions.add(createJavaQuestion("Spring'te Bean Scope'ları nelerdir?",
                        List.of("Sadece singleton ve prototype vardır",
                            "Sadece request ve session vardır",
                            "singleton, prototype, request, session, application",
                            "Sadece application scope vardır"),
                        "C",
                        "Spring'te temel bean scope'ları: singleton (varsayılan), prototype, request, session ve application'dır."));
                    break;

                case SQL:
                    questions.add(generateMockQuestion(category, 0)); // INNER JOIN sorusu
                    questions.add(createJavaQuestion("SQL'de GROUP BY ne işe yarar?",
                        List.of("Sadece sıralama yapar",
                            "Satırları belirli kriterlere göre gruplar ve aggregate fonksiyonlarla kullanılır",
                            "Sadece filtreleme yapar",
                            "Sadece birleştirme yapar"),
                        "B",
                        "GROUP BY, verileri belirli kolonlara göre gruplar ve COUNT, SUM gibi aggregate fonksiyonlarla birlikte kullanılır."));
                    questions.add(createJavaQuestion("SQL'de INDEX'ler ne işe yarar?",
                        List.of("Sadece primary key tanımlamak için kullanılır",
                            "Sadece foreign key tanımlamak için kullanılır",
                            "Sorgu performansını artırmak için kullanılır",
                            "Sadece unique constraint eklemek için kullanılır"),
                        "C",
                        "INDEX'ler, sorgu performansını artırmak için kullanılır. Sık sorgulanan kolonlar üzerinde index oluşturularak arama hızı artırılır."));
                    break;

                case DESIGN_PATTERNS:
                    questions.add(generateMockQuestion(category, 0)); // Singleton sorusu
                    questions.add(createJavaQuestion("Factory Design Pattern ne işe yarar?",
                        List.of("Sadece singleton nesneler oluşturmak için",
                            "Nesne oluşturma sürecini soyutlamak için",
                            "Sadece proxy nesneler oluşturmak için",
                            "Sadece builder nesneler oluşturmak için"),
                        "B",
                        "Factory pattern, nesne oluşturma sürecini soyutlar ve client kodun concrete class'lara bağımlılığını azaltır."));
                    questions.add(createJavaQuestion("Observer Design Pattern hangi durumda kullanılır?",
                        List.of("Sadece singleton nesneler için",
                            "Sadece factory nesneler için",
                            "Bir nesnenin durumu değiştiğinde diğer nesneleri haberdar etmek için",
                            "Sadece builder nesneler için"),
                        "C",
                        "Observer pattern, bir nesnenin durumu değiştiğinde bağımlı nesneleri otomatik olarak haberdar etmek için kullanılır."));
                    break;
            }
            return questions;
        } catch (Exception e) {
            log.error("Sorular üretilirken hata oluştu: ", e);
            throw new ExternalApiException("Sorular üretilirken hata oluştu: " + e.getMessage());
        }
    }

    private QuestionDTO createJavaQuestion(String question, List<String> options, String correctAnswer, String explanation) {
        return QuestionDTO.builder()
                .question(question)
                .options(options)
                .correctAnswer(correctAnswer)
                .explanation(explanation)
                .build();
    }

    private QuestionDTO generateSingleQuestion(TestCategory category) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiToken);

            HuggingFaceRequestDTO request = createRequest(category);
            HttpEntity<HuggingFaceRequestDTO> entity = new HttpEntity<>(request, headers);

            log.debug("HuggingFace API isteği gönderiliyor: {}", request);
            
            HuggingFaceResponseDTO response =
                    restTemplate.postForObject(apiUrl, entity, HuggingFaceResponseDTO.class);

            log.debug("HuggingFace API yanıtı alındı: {}", response);

            if (response == null || response.getGeneratedText() == null) {
                log.error("HuggingFace API null yanıt döndü");
                return generateMockQuestion(category, 0);
            }

            String generatedText = response.getGeneratedText();
            if (generatedText.trim().isEmpty()) {
                log.error("HuggingFace API boş metin döndü");
                return generateMockQuestion(category, 0);
            }

            return parseResponse(generatedText);
        } catch (Exception e) {
            log.error("HuggingFace API çağrısı sırasında hata: ", e);
            return generateMockQuestion(category, 0);
        }
    }

    private HuggingFaceRequestDTO createRequest(TestCategory category) {
        String prompt = String.format("""
            [INST] You are a Java programming expert. Create a multiple choice question about %s.
            Format the response exactly like this:
            Question: (the question)
            A) (first option)
            B) (second option)
            C) (third option)
            D) (fourth option)
            Correct Answer: (letter of correct option)
            Explanation: (why this is the correct answer)
            [/INST]
            """, 
            category.toString());

        HuggingFaceRequestDTO.Parameters params = HuggingFaceRequestDTO.Parameters.builder()
                .maxLength(1000)
                .temperature(0.7)
                .topK(50)
                .topP(0.9)
                .build();

        return HuggingFaceRequestDTO.builder()
                .prompt(prompt)
                .parameters(params)
                .build();
    }

    private QuestionDTO parseResponse(String generatedText) {
        try {
            log.debug("API yanıtı parse ediliyor: {}", generatedText);
            
            // Question: ile başlayan satırı bul
            String[] parts = generatedText.split("Question:");
            if (parts.length < 2) {
                log.error("Yanıt formatı hatalı - Question: bulunamadı");
                throw new IllegalArgumentException("Invalid response format");
            }

            String[] lines = parts[1].split("\n");
            String question = lines[0].trim();
            List<String> options = new ArrayList<>();
            String correctAnswer = null;
            String explanation = null;

            for (String line : lines) {
                line = line.trim();
                if (line.startsWith("A)")) options.add(line.substring(2).trim());
                else if (line.startsWith("B)")) options.add(line.substring(2).trim());
                else if (line.startsWith("C)")) options.add(line.substring(2).trim());
                else if (line.startsWith("D)")) options.add(line.substring(2).trim());
                else if (line.startsWith("Correct Answer:")) correctAnswer = line.substring(15).trim();
                else if (line.startsWith("Explanation:")) explanation = line.substring(12).trim();
            }

            if (options.size() != 4 || correctAnswer == null || explanation == null) {
                log.error("Eksik veri - options: {}, correctAnswer: {}, explanation: {}", 
                    options.size(), correctAnswer, explanation);
                throw new IllegalArgumentException("Missing required fields");
            }

            return QuestionDTO.builder()
                    .question(question)
                    .options(options)
                    .correctAnswer(correctAnswer)
                    .explanation(explanation)
                    .build();
        } catch (Exception e) {
            log.error("Yanıt parse edilirken hata oluştu: ", e);
            throw e;
        }
    }

    private QuestionDTO generateMockQuestion(TestCategory category, int index) {
        String question = "";
        List<String> options = new ArrayList<>();
        String correctAnswer = "";
        String explanation = "";

        switch (category) {
            case CORE_JAVA:
                question = "Java'da 'final' keyword'ü ne işe yarar?";
                options = List.of(
                    "Sadece değişkenleri sabit yapmak için kullanılır",
                    "Sadece metodları override edilemez yapmak için kullanılır",
                    "Sadece sınıfları extend edilemez yapmak için kullanılır",
                    "Değişken, metod ve sınıflar için farklı amaçlarla kullanılabilir"
                );
                correctAnswer = "D";
                explanation = "final keyword'ü Java'da çok yönlü kullanılır: 1) Değişkenler için: değer değiştirilemez, 2) Metodlar için: override edilemez, 3) Sınıflar için: extend edilemez yapar.";
                break;

            case SPRING_FRAMEWORK:
                question = "Spring Framework'te @Autowired anotasyonu ne işe yarar?";
                options = List.of(
                    "Sadece constructor injection için kullanılır",
                    "Sadece setter injection için kullanılır",
                    "Dependency injection işlemlerini otomatikleştirir",
                    "Sadece field injection için kullanılır"
                );
                correctAnswer = "C";
                explanation = "@Autowired, Spring'in dependency injection mekanizmasını otomatikleştiren bir anotasyondur. Constructor, setter veya field injection için kullanılabilir.";
                break;

            case SQL:
                question = "SQL'de INNER JOIN ne işe yarar?";
                options = List.of(
                    "İki tablonun tüm kayıtlarını birleştirir",
                    "Sadece eşleşen kayıtları birleştirir",
                    "Sol tablodaki tüm kayıtları getirir",
                    "Sağ tablodaki tüm kayıtları getirir"
                );
                correctAnswer = "B";
                explanation = "INNER JOIN, iki tablo arasında sadece eşleşen (matching) kayıtları getirir. Eşleşmeyen kayıtlar sonuç kümesine dahil edilmez.";
                break;

            case DESIGN_PATTERNS:
                question = "Singleton tasarım deseni ne amaçla kullanılır?";
                options = List.of(
                    "Nesne oluşturma sürecini standartlaştırmak için",
                    "Bir sınıftan sadece bir nesne oluşturulmasını sağlamak için",
                    "Nesneler arası iletişimi düzenlemek için",
                    "Karmaşık nesneleri basitleştirmek için"
                );
                correctAnswer = "B";
                explanation = "Singleton pattern, bir sınıftan tüm uygulama boyunca sadece bir nesne (instance) oluşturulmasını garanti eder. Genellikle global state yönetimi için kullanılır.";
                break;
        }

        return QuestionDTO.builder()
                .question(question)
                .options(options)
                .correctAnswer(correctAnswer)
                .explanation(explanation)
                .build();
    }
}
