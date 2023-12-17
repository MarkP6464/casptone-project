package com.example.capstoneproject.service.impl;

import com.example.capstoneproject.Config.OpenAIConfig;
import com.example.capstoneproject.entity.Admin;
import com.example.capstoneproject.entity.Users;
import com.example.capstoneproject.repository.AdminRepository;
import com.example.capstoneproject.service.UsersService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class ChatGPTServiceImpl {
    private final String chatGptApiUrl = "https://api.openai.com/v1/chat/completions";

    @Autowired
    OpenAIConfig openAIConfig;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    public ChatGPTServiceImpl(OpenAIConfig openAIConfig, RestTemplate restTemplate) {
        this.openAIConfig = openAIConfig;
        this.restTemplate = restTemplate;
    }

    public String chatWithGPT(String message, float temperature) {
        HttpHeaders headers = new HttpHeaders();
        Admin users = adminRepository.getById(1);
        headers.setBearerAuth(users.getConfiguration().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"model\":\"gpt-3.5-turbo\",\"messages\":" + message + ",\"temperature\":" + temperature + "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                chatGptApiUrl, HttpMethod.POST, requestEntity, String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Phân tích JSON và lấy nội dung
            String responseBody = responseEntity.getBody();
            return extractContentFromResponse(responseBody);
        } else {
            throw new RuntimeException("Error communicating with ChatGPT");
        }
    }

    public String chatWithGPTCoverLetter(String message, float temperature) {
        HttpHeaders headers = new HttpHeaders();
        Admin users = adminRepository.getById(1);
        headers.setBearerAuth(users.getConfiguration().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"model\":\"gpt-3.5-turbo\",\"messages\":" + message + ",\"temperature\":" + temperature + "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                chatGptApiUrl, HttpMethod.POST, requestEntity, String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Phân tích JSON và lấy nội dung
            String responseBody = responseEntity.getBody();
            return extractContentFromResponse(responseBody);
        } else {
            throw new RuntimeException("Error communicating with ChatGPT");
        }
    }

    public String chatWithGPTCoverLetterRevise(String message) {
        HttpHeaders headers = new HttpHeaders();
        Admin users = adminRepository.getById(1);
        headers.setBearerAuth(users.getConfiguration().getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = "{\"model\":\"gpt-3.5-turbo\",\"messages\":" + message + "}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                chatGptApiUrl, HttpMethod.POST, requestEntity, String.class
        );

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Phân tích JSON và lấy nội dung
            String responseBody = responseEntity.getBody();
            return extractContentFromResponse(responseBody);
        } else {
            throw new RuntimeException("Error communicating with ChatGPT");
        }
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            if (jsonNode.has("choices")) {
                JsonNode choicesNode = jsonNode.get("choices");
                if (choicesNode.isArray() && choicesNode.size() > 0) {
                    JsonNode choice = choicesNode.get(0);
                    if (choice.has("message") && choice.get("message").has("content")) {
                        return choice.get("message").get("content").asText();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
