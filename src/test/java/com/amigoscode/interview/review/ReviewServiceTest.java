package com.amigoscode.interview.review;

import com.amigoscode.interview.book.Book;
import com.amigoscode.interview.book.BookRepository;
import com.amigoscode.interview.book.BookService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    BookRepository bookRepository;
    @Mock
    ReviewRepository reviewRepository;
    @Mock
    ReviewRequest reviewRequest;
    @Mock
    Book book;
    @Mock
    Review review;
    @Mock
    ReviewMapper reviewMapper;
    @InjectMocks
    ReviewService reviewService;

    @Test
    void getReviews() {
        PageRequest pageRequest = PageRequest.of(1, 10, Sort.by("DESC"));
        Page<Review> mockPage = new PageImpl<>(List.of(review, review), pageRequest, 2);
        when(reviewRepository.findByBookId(anyLong(), any(PageRequest.class))).thenReturn(mockPage);

        Page<ReviewResponse> result = reviewService.getReviews(1L, pageRequest);

        assertEquals(2, result.stream().count());
    }

    @Test
    void createReview() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(reviewMapper.toEntity(any())).thenReturn(review);

        reviewService.createReview(1L, reviewRequest);

        verify(reviewRepository, times(1)).save(any());
    }
}