package com.amigoscode.interview.book;

import com.amigoscode.interview.config.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // N+1 problem: fetching all books triggers separate queries
    // for each book's author and reviews due to lazy loading
    public Page<Book> getAllBooks(PageRequest pageRequest) {
        Page<Book> books = bookRepository.findAll(pageRequest);
        // Accessing author name forces lazy load — one query per book
        books.forEach(book -> {
            if (book.getAuthor() != null) {
                book.getAuthor().getName();
            }
        });
        return books;
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Book with id:%s not found", id)));
    }

    public List<Book> searchBooks(String genre, String authorName) {
        return bookRepository.searchBooks(genre, authorName);
    }

}
