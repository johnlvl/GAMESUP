package com.gamesUP.gamesUP.web.controller;

import com.gamesUP.gamesUP.service.PublisherService;
import com.gamesUP.gamesUP.web.dto.PublisherDTO;
import com.gamesUP.gamesUP.web.error.GlobalExceptionHandler;
import com.gamesUP.gamesUP.config.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PublisherController.class)
@Import({GlobalExceptionHandler.class, SecurityConfig.class})
class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublisherService service;

    @TestConfiguration
    static class TestConfig {
        @Bean
        PublisherService publisherService() {
            return Mockito.mock(PublisherService.class);
        }
    }

    @Test
    void list_ok() throws Exception {
        PublisherDTO p = new PublisherDTO(); p.id = 1L; p.name = "Asmodee";
        Page<PublisherDTO> page = new PageImpl<>(List.of(p), PageRequest.of(0, 20), 1);
        Mockito.when(service.list(any())).thenReturn(page);

        mockMvc.perform(get("/api/publishers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name", is("Asmodee")));
    }

    @Test
    void create_201() throws Exception {
        PublisherDTO created = new PublisherDTO(); created.id = 5L; created.name = "Repos Prod";
        Mockito.when(service.create(any())).thenReturn(created);

        String body = "{\n  \"name\": \"Repos Prod\"\n}";
        mockMvc.perform(post("/api/publishers").with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/api/publishers/5")))
                .andExpect(jsonPath("$.id", is(5)));
    }

    @Test
    void get_notFound_404() throws Exception {
        Mockito.when(service.get(anyLong())).thenThrow(new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/api/publishers/42")).andExpect(status().isNotFound());
    }

    @Test
    void update_ok() throws Exception {
        PublisherDTO updated = new PublisherDTO(); updated.id = 2L; updated.name = "Matagot";
        Mockito.when(service.update(eq(2L), any())).thenReturn(updated);
        mockMvc.perform(put("/api/publishers/2").with(httpBasic("admin","admin")).contentType(MediaType.APPLICATION_JSON).content("{\n \"name\": \"Matagot\"\n}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Matagot")));
    }

    @Test
    void delete_noContent() throws Exception {
        mockMvc.perform(delete("/api/publishers/2").with(httpBasic("admin","admin"))).andExpect(status().isNoContent());
        Mockito.verify(service).delete(2L);
    }
}
