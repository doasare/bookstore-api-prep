package com.amigoscode.interview.review;

import com.amigoscode.interview.book.Book;
import com.amigoscode.interview.book.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

// No service layer — repository injected directly (intentional issue)
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public ReviewController(ReviewRepository reviewRepository,
                            BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<Review>> getReviewsByBookId(@PathVariable Long bookId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String sort ) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        Page<Review> reviews = reviewRepository.findByBookId(bookId, pageRequest);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/book/{bookId}")
    public ResponseEntity<Review> createReview(
            @PathVariable Long bookId,
            @RequestBody Review review) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        review.setBook(book);
        review.setCreatedAt(LocalDateTime.now());
        Review saved = reviewRepository.save(review);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

}
