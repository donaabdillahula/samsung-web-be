package com.dona_samsung_web_project.samsung_web_be.repository;

import com.dona_samsung_web_project.samsung_web_be.model.Book;
import com.dona_samsung_web_project.samsung_web_be.model.BookStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books by category (case-insensitive search)
    Page<Book> findByCategoryContainingIgnoreCase(String category, Pageable pageable);

    // Find books by title (case-insensitive search)
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("""
                SELECT b FROM Book b
                LEFT JOIN BorrowedBook bb ON bb.book = b
                GROUP BY b
                ORDER BY COUNT(bb.id) DESC
            """)
    Page<Book> findTopBooksByBorrowedCount(Pageable pageable);

    Page<Book> findByStatusOrderByCreatedAtDesc(BookStatus status, Pageable pageable);
}
