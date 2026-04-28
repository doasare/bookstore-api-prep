package com.amigoscode.interview.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @EntityGraph(attributePaths = {"author"})
    Page<Book> findAll(Pageable pageable);

    @Query("SELECT b FROM Book b WHERE " +
            "(:genre IS NULL OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
            "(:authorName IS NULL OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :authorName, '%')))")
    List<Book> searchBooks(
            @Param("genre") String genre,
            @Param("authorName") String authorName
    );

    @Query("SELECT b FROM Book b JOIN b.reviews r GROUP BY b ORDER BY AVG(r.rating) DESC")
    List<Book> findTop5ByOrderByReviewRatingDesc(Pageable pageable);

    @Query("SELECT b FROM Book b JOIN b.reviews r GROUP BY b ORDER BY COUNT(r) DESC")
    List<Book> findTopBooks(Pageable pageable);

    @Query("SELECT new com.amigoscode.interview.book.StatsService$GenreRating(b.genre, AVG(r.rating)) " +
            "FROM Book b JOIN b.reviews r GROUP BY b.genre")
    List<StatsService.GenreRating> findAvgRatingPerGenre();
}
