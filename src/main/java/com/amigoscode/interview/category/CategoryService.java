package com.amigoscode.interview.category;

import com.amigoscode.interview.book.Book;
import com.amigoscode.interview.book.BookRepository;
import com.amigoscode.interview.config.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;
    final BookRepository bookRepository;

    public List<CategoryController.CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryController.CategoryResponse createCategory(CategoryController.CategoryRequest categoryRequest) {
        Category category = categoryMapper
                .toEntity(categoryRequest);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public CategoryController.CategoryResponse getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Category for id %s not found", id)));
    }

    public void addBooksToCategory(CategoryController.AddBookToCategoryRequest addBookToCategoryRequest) {
        Category category = categoryRepository.findById(addBookToCategoryRequest.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Category for id %s not found", addBookToCategoryRequest.categoryId())));
        Set<Book> books = Arrays.stream(addBookToCategoryRequest.ids())
                .mapToObj(bookRepository::findById)
                .flatMap(Optional::stream)
                .collect(Collectors.toSet());
        category.getBooks().addAll(books);
        categoryRepository.save(category);
    }

}
