package com.amigoscode.interview.book;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {
    final BookRepository bookRepository;
    public record GenreRating(String genre, Double avgRating) {}

    public record BookStatsResponse(
            List<Book> topRatedBooks,
            List<Book> mostReviewedBooks,
            List<GenreRating> avgRatingPerGenre
    ) {}

    private List<Book> getTop5RatedBooks() {
        return bookRepository.findTop5ByOrderByReviewRatingDesc(PageRequest.ofSize(5));
    }

    private List<Book> findMostReviewedBooks(){
        return bookRepository.findTopBooks(PageRequest.ofSize(5));
    }

    private List<GenreRating> findAvgRatingPerGenre(){
        return bookRepository.findAvgRatingPerGenre();
    }

    @Cacheable("getLibraryStats")
    public BookStatsResponse getBookStats() {
        return new BookStatsResponse(
                getTop5RatedBooks(),
                findMostReviewedBooks(),
                findAvgRatingPerGenre()
        );
    }


}

