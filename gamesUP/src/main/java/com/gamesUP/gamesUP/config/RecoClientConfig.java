package com.gamesUP.gamesUP.config;

import com.gamesUP.gamesUP.service.RecoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RecoClientConfig {

    @Bean
    public RecoClient recoClient(@Value("${reco.base-url:http://localhost:8000}") String baseUrl) {
        return new RecoClient(baseUrl);
    }
}
