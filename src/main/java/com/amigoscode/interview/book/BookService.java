package com.amigoscode.interview.book;

import com.amigoscode.interview.config.ResourceNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // N+1 problem: fetching all books triggers separate queries
    // for each book's author and reviews due to lazy loading
    @Cacheable(value = "getAllBooks", key = "#pageRequest.pageNumber + '-' + #pageRequest.pageSize")
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

    @CacheEvict(value = {"getAllBooks", "searchBooks", "getBook"}, allEntries = true)
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @CacheEvict(value = {"getAllBooks", "searchBooks", "getBook"}, allEntries = true)
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

    @Cacheable(value = "getBook", key = "#id")
    public Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String
                        .format("Book with id:%s not found", id)));
    }

    @Cacheable(value = "searchBooks", key = "#genre + '-' + #authorName")
    public List<Book> searchBooks(String genre, String authorName) {
        return bookRepository.searchBooks(genre, authorName);
    }

}
