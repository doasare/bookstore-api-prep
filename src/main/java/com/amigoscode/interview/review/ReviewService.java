package com.amigoscode.interview.review;

import com.amigoscode.interview.book.Book;
import com.amigoscode.interview.book.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;
    private final ReviewMapper reviewMapper;

    public Page<ReviewResponse> getReviews(long bookId, PageRequest pageRequest) {
        return reviewRepository.findByBookId(bookId, pageRequest)
                .map(reviewMapper::toResponse);
    }

    public ReviewResponse createReview(Long bookId, ReviewRequest reviewRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        Review review = reviewMapper.toEntity(reviewRequest);
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toResponse(savedReview);
    }
}