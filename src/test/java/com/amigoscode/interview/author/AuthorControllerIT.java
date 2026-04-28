package com.amigoscode.interview.author;

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
class AuthorControllerIT {

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
    void getAllAuthors() throws Exception {
        mockMvc.perform(get("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getAuthorById() throws Exception {
        mockMvc.perform(get("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("George Orwell"));
    }

    @Test
    void getAuthorById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/authors/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("SERVER_ERR"))
                .andExpect(jsonPath("$.message.error_message").value("Author not found with id: 999"));
    }

    @Test
    void createAuthor() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "Fyodor Dostoevsky",
                "bio", "Russian novelist, best known for Crime and Punishment."
        );

        mockMvc.perform(post("/api/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Fyodor Dostoevsky"))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void updateAuthor() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "George Orwell Updated",
                "bio", "Updated bio."
        );

        mockMvc.perform(put("/api/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("George Orwell Updated"))
                .andExpect(jsonPath("$.bio").value("Updated bio."));
    }

    @Test
    void updateAuthor_notFound_returns404() throws Exception {
        Map<String, Object> body = Map.of(
                "name", "Nobody",
                "bio", "Does not exist."
        );

        mockMvc.perform(put("/api/authors/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("SERVER_ERR"));
    }

    @Test
    void deleteAuthor() throws Exception {
        mockMvc.perform(delete("/api/authors/3"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAuthor_notFound_returns404() throws Exception {
        mockMvc.perform(delete("/api/authors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("SERVER_ERR"));
    }
}
