package com.amigoscode.interview.review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
class ReviewControllerIT {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void getReviewsByBookId() throws Exception {
        // Book 1 (1984) has 2 seeded reviews
        mockMvc.perform(get("/api/reviews/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.content[0].reviewerName").exists())
                .andExpect(jsonPath("$.content[0].rating").exists())
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void getReviewsByBookId_noReviews_returnsEmptyPage() throws Exception {
        mockMvc.perform(get("/api/reviews/book/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void createReview() throws Exception {
        Map<String, Object> body = Map.of(
                "reviewerName", "Test Reviewer",
                "content", "An excellent read from start to finish.",
                "rating", 5
        );

        mockMvc.perform(post("/api/reviews/book/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.reviewerName").value("Test Reviewer"))
                .andExpect(jsonPath("$.content").value("An excellent read from start to finish."))
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createReview_bookNotFound_returns404() throws Exception {
        Map<String, Object> body = Map.of(
                "reviewerName", "Test Reviewer",
                "content", "Great book.",
                "rating", 4
        );

        mockMvc.perform(post("/api/reviews/book/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("SERVER_ERR"))
                .andExpect(jsonPath("$.message.error_message").value("Book not found with id: 999"));
    }

    @Test
    void getReviewsByBookId_pagination() throws Exception {
        mockMvc.perform(get("/api/reviews/book/1")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(2));
    }
}
