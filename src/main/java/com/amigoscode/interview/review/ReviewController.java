package com.amigoscode.interview.review;

import com.amigoscode.interview.book.Book;
import com.amigoscode.interview.book.BookRepository;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Page<ReviewResponse>> getReviewsByBookId(@PathVariable Long bookId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @RequestParam(defaultValue = "") String sort ) {
        Page<ReviewResponse> reviews = reviewService.getReviews(bookId, PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/book/{bookId}")
    public ResponseEntity<ReviewResponse> createReview(
            @PathVariable Long bookId,
            @RequestBody ReviewRequest reviewRequest) {
        ReviewResponse saved = reviewService.createReview(bookId, reviewRequest);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }
}
