package com.amigoscode.interview.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class BookControllerIT {

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
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/api/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").exists())
                .andExpect(jsonPath("$.content[0].isbn").exists())
                .andExpect(jsonPath("$.totalElements").value(10));
    }

    @Test
    void createBook() throws Exception {
        Map<String, Object> body = Map.of(
                "title", "Clean Code",
                "isbn", "978-0132350884",
                "price", 29.99
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.isbn").value("978-0132350884"));
    }

    @Test
    void createBook_blankTitle_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "title", "",
                "isbn", "978-0132350884"
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message.title").value("Title should not be blank"));
    }

    @Test
    void createBook_negativePrice_returns400() throws Exception {
        Map<String, Object> body = Map.of(
                "title", "Clean Code",
                "isbn", "978-0132350884",
                "price", -5.00
        );

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message.price").value("must be greater than 0"));
    }

    @Test
    void getBook() throws Exception {
        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    void getBook_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/books/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message.error_message").value("Book with id:999 not found"));
    }

    @Test
    void deleteBook() throws Exception {
        mockMvc.perform(delete("/api/books/2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchBooks_byGenre() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("genre", "Fantasy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].genre").value("Fantasy"));
    }

    @Test
    void searchBooks_noResults() throws Exception {
        mockMvc.perform(get("/api/books/search")
                        .param("genre", "NonExistentGenre")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
