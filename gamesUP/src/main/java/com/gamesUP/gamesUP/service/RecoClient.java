package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.reco.*;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import java.net.http.HttpClient;

public class RecoClient {

    private final RestClient restClient;

    public RecoClient(String baseUrl) {
        HttpClient httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory(httpClient);
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(factory)
                .build();
    }

    public RecoClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public HealthResponse health() {
        return restClient.get()
                .uri("/health")
                .retrieve()
                .body(HealthResponse.class);
    }

    public RecommendationsResponse recommendations(RecommendationsRequest request) {
        return restClient.post()
                .uri("/recommendations")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(RecommendationsResponse.class);
    }

    public EventsBatchResponse eventsBatch(EventsBatchRequest request) {
        return restClient.post()
                .uri("/events/batch")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(EventsBatchResponse.class);
    }
}
