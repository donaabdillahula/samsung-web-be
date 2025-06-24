package com.dona_samsung_web_project.samsung_web_be.repository;

import com.dona_samsung_web_project.samsung_web_be.model.BorrowedBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowedBookRepository extends JpaRepository<BorrowedBook, Long> {

    // Find borrowed books by member ID with pagination
    Page<BorrowedBook> findByMemberId(Long memberId, Pageable pageable);

    // Find borrowed books by book ID with pagination
    Page<BorrowedBook> findByBookId(Long bookId, Pageable pageable);

    // Find borrowed books by book title, member name, and borrow date with
    // pagination
    Page<BorrowedBook> findByBookTitleContainingIgnoreCaseOrMemberNameContainingIgnoreCaseOrBorrowDate(
            String bookTitle, String memberName, java.time.LocalDate borrowDate, Pageable pageable);
}
