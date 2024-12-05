package com.erkan.interview_test_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.PostConstruct;

@Configuration
@Slf4j
public class OpenAIConfig {

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        log.info("OpenAI API Key loaded: {}", apiKey);
    }

    @Bean(name = "openAIRestTemplate")
    public RestTemplate openAIRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        ClientHttpRequestInterceptor interceptor = (request, body, execution) -> {
            HttpHeaders headers = request.getHeaders();

            // Authorization header'ını ekle
            String authHeader = "Bearer " + apiKey;
            headers.set("Authorization", authHeader);

            // Content-Type ve Accept header'larını ekle
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Debug için header'ları logla
            log.debug("Authorization Header: {}", authHeader);
            log.debug("Content-Type Header: {}", headers.getContentType());
            log.debug("Accept Header: {}", headers.getAccept());
            log.debug("All Headers: {}", headers);

            return execution.execute(request, body);
        };

        restTemplate.getInterceptors().add(interceptor);
        return restTemplate;
    }
}