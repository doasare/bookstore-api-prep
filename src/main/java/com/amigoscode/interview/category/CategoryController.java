package com.amigoscode.interview.category;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public record CategoryRequest(@NotBlank(message = "Name should not be blank") String name){}
    public record CategoryResponse(Long id, String name){}
    public record AddBookToCategoryRequest(long categoryId, long[] ids){}

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@RequestBody @Valid CategoryRequest categoryRequest) {
        CategoryResponse created = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategory(@PathVariable Long id){
        return ResponseEntity.ok(categoryService.getCategoryById(id));
    }

    @PutMapping("/books")
    public ResponseEntity<Void> addBookToCategory(@RequestBody AddBookToCategoryRequest addBookToCategoryRequest) {
        categoryService.addBooksToCategory(addBookToCategoryRequest);
        return ResponseEntity.noContent().build();
    }
}
