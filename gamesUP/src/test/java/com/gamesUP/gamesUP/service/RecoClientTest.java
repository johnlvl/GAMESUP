package com.gamesUP.gamesUP.service;

import com.gamesUP.gamesUP.web.dto.reco.EventsBatchRequest;
import com.gamesUP.gamesUP.web.dto.reco.EventsBatchResponse;
import com.gamesUP.gamesUP.web.dto.reco.HealthResponse;
import com.gamesUP.gamesUP.web.dto.reco.RecommendationItem;
import com.gamesUP.gamesUP.web.dto.reco.RecommendationsRequest;
import com.gamesUP.gamesUP.web.dto.reco.RecommendationsResponse;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.*;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

class RecoClientTest {

    static WireMockServer wm;
    RecoClient client;

    @BeforeAll
    static void startWireMock() {
        wm = new WireMockServer(0);
        wm.start();
        configureFor("localhost", wm.port());
    }

    @AfterAll
    static void stopWireMock() {
        wm.stop();
    }

    @BeforeEach
    void setup() {
        client = new RecoClient("http://localhost:" + wm.port());
    }

    @Test
    void health_ok() {
        wm.stubFor(get(urlEqualTo("/health"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"status\":\"ok\",\"model_loaded\":true}")));

        HealthResponse res = client.health();
        assertThat(res.status).isEqualTo("ok");
        assertThat(res.model_loaded).isTrue();
    }

    @Test
    void recommendations_ok() {
        wm.stubFor(post(urlEqualTo("/recommendations"))
                .withRequestBody(matchingJsonPath("$.user_id", equalTo("123")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\n  \"user_id\":123, \n  \"recommendations\":[{\"game_id\":10,\"score\":0.92}],\n  \"model_version\":\"v1\"\n}")));

        RecommendationsRequest req = new RecommendationsRequest();
        req.user_id = 123L; req.k = 1; req.candidates = List.of(10);
        RecommendationsResponse res = client.recommendations(req);
        assertThat(res.user_id).isEqualTo(123L);
        assertThat(res.recommendations).hasSize(1);
        RecommendationItem it = res.recommendations.get(0);
        assertThat(it.game_id).isEqualTo(10);
        assertThat(it.score).isEqualTo(0.92);
        assertThat(res.model_version).isEqualTo("v1");
    }

    @Test
    void events_batch_ok() {
        wm.stubFor(post(urlEqualTo("/events/batch"))
                .withRequestBody(matchingJsonPath("$.events[0].type", equalTo("VIEW")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"accepted\":1}")));

        EventsBatchRequest req = new EventsBatchRequest();
        EventsBatchRequest.EventItem e = new EventsBatchRequest.EventItem();
        e.type = "VIEW"; e.user_id = 5L; e.game_id = 10; e.ts = "2025-11-18T10:00:00Z";
        req.events = List.of(e);
        EventsBatchResponse res = client.eventsBatch(req);
        assertThat(res.accepted).isEqualTo(1);
    }
}
