package com.amigoscode.interview.book;

import com.amigoscode.interview.author.Author;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        when(bookRepository.findAll()).thenReturn(List.of(book, book));

        List<Book> result = bookService.getAllBooks();

        assertEquals(result.size(), 2);
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
}