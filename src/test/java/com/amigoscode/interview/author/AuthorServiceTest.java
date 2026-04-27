package com.amigoscode.interview.author;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock AuthorRepository authorRepository;
    @Mock Author author;
    @InjectMocks
    AuthorService authorService;

    @Test
    void getAllAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(author, author));

        List<Author> result = authorService.getAllAuthors();

        assertEquals(2, result.size());
    }

    @Test
    void getAuthorById() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));

        Author author = authorService.getAuthorById(1L);

        assertNotNull(author);
    }

    @Test
    void createAuthor() {
        authorService.createAuthor(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void updateAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.updateAuthor(1L, author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void deleteAuthor() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        authorService.deleteAuthor(1L);

        verify(authorRepository, times(1)).delete(author);
    }
}