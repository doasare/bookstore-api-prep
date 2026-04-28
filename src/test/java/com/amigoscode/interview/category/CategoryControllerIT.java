package com.amigoscode.interview.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class CategoryControllerIT {

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
    void getAllCategories() throws Exception {
        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void createCategory() throws Exception {
        Map<String, Object> body = Map.of("name", "Science Fiction");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Science Fiction"));
    }

    @Test
    void createCategory_blankName_returns400() throws Exception {
        Map<String, Object> body = Map.of("name", "");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message.name").exists());
    }

    @Test
    void getCategory() throws Exception {
        // create a category first, then fetch it
        Map<String, Object> body = Map.of("name", "Fantasy");
        String response = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").longValue();

        mockMvc.perform(get("/api/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Fantasy"));
    }

    @Test
    void getCategory_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/categories/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message.error_message").value("Category for id 999 not found"));
    }

    @Test
    void deleteCategory() throws Exception {
        Map<String, Object> body = Map.of("name", "Horror");
        String response = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(response).get("id").longValue();

        mockMvc.perform(delete("/api/categories/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void addBooksToCategory() throws Exception {
        Map<String, Object> body = Map.of("name", "Classic");
        String response = mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andReturn().getResponse().getContentAsString();

        Long categoryId = objectMapper.readTree(response).get("id").longValue();

        Map<String, Object> addRequest = Map.of("categoryId", categoryId, "ids", new long[]{1L, 2L});

        mockMvc.perform(put("/api/categories/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isNoContent());
    }

    @Test
    void addBooksToCategory_categoryNotFound_returns404() throws Exception {
        Map<String, Object> addRequest = Map.of("categoryId", 999L, "ids", new long[]{1L});

        mockMvc.perform(put("/api/categories/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("RESOURCE_NOT_FOUND"))
                .andExpect(jsonPath("$.message.error_message").value("Category for id 999 not found"));
    }
}
