package com.amigoscode.interview.book;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAllBooks(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String sort ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        return ResponseEntity.ok(bookService.getAllBooks(pageRequest));
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book book) {
        Book created = bookService.createBook(book);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id){
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam(required = false) String genre, @RequestParam(required = false) String authorName){
        return ResponseEntity.ok(bookService.searchBooks(genre, authorName));
    }


}
