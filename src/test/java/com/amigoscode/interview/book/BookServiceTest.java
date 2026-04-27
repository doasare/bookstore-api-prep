package com.amigoscode.interview.book;

import com.amigoscode.interview.author.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    BookRepository bookRepository;
    @Mock
    Book book;
    @InjectMocks
    BookService bookService;

    @Test
    void getAllBooks() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("DESC"));
        Page<Book> mockPage = new PageImpl<>(List.of(book, book), pageRequest, 2);
        when(bookRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        Page<Book> result = bookService.getAllBooks(pageRequest);

        assertEquals(2, result.stream().count());
    }

    @Test
    void createBook() {
        bookService.createBook(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void deleteBook() {
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void getBook() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book book = bookService.getBook(1L);

        assertNotNull(book);
    }

    @Test
    void searchBooks() {
        when(bookRepository.searchBooks(anyString(), anyString())).thenReturn(List.of(book, book));

        List<Book> results = bookService.searchBooks("", "George");

        assertEquals(2, results.size());
    }
}