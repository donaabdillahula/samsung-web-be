package com.dona_samsung_web_project.samsung_web_be.service;

import com.dona_samsung_web_project.samsung_web_be.exception.EntityExistException;
import com.dona_samsung_web_project.samsung_web_be.exception.NotFoundException;
import com.dona_samsung_web_project.samsung_web_be.model.Book;
import com.dona_samsung_web_project.samsung_web_be.model.BookStatus;
import com.dona_samsung_web_project.samsung_web_be.model.BorrowedBook;
import com.dona_samsung_web_project.samsung_web_be.model.Member;
import com.dona_samsung_web_project.samsung_web_be.repository.BookRepository;
import com.dona_samsung_web_project.samsung_web_be.repository.BorrowedBookRepository;
import com.dona_samsung_web_project.samsung_web_be.repository.MemberRepository;

import jakarta.validation.ValidationException;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BorrowedBookService {

    private final BorrowedBookRepository borrowedBookRepository;
    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;

    public BorrowedBookService(BorrowedBookRepository borrowedBookRepository, BookRepository bookRepository,
            MemberRepository memberRepository) {
        this.borrowedBookRepository = borrowedBookRepository;
        this.bookRepository = bookRepository;
        this.memberRepository = memberRepository;
    }

    // Save or update a borrowed book
    public BorrowedBook saveBorrowedBook(BorrowedBook borrowedBook) {
        // Validate book
        if (borrowedBook.getBook() == null || borrowedBook.getBook().getId() == null) {
            throw new NotFoundException("Book is required");
        }
        Book newBook = bookRepository.findById(borrowedBook.getBook().getId())
                .orElseThrow(() -> new NotFoundException("Book not found"));

        // Validate member
        if (borrowedBook.getMember() == null || borrowedBook.getMember().getId() == null) {
            throw new NotFoundException("Member is required");
        }
        Member member = memberRepository.findById(borrowedBook.getMember().getId())
                .orElseThrow(() -> new NotFoundException("Member not found"));

        Book oldBook = null;
        if (borrowedBook.getId() != null) {
            BorrowedBook existing = borrowedBookRepository.findById(borrowedBook.getId())
                    .orElseThrow(() -> new NotFoundException("BorrowedBook not found"));

            // If book id changes, revert old book status to AVAILABLE
            if (!existing.getBook().getId().equals(newBook.getId())) {
                oldBook = existing.getBook();
                oldBook.setStatus(BookStatus.AVAILABLE);
            }
        }

        // Check new book status
        if (newBook.getStatus() != BookStatus.AVAILABLE) {
            throw new EntityExistException("Book with id " + newBook.getId() + " is not available for borrowing");
        }

        // Set borrow date if new
        if (borrowedBook.getId() == null) {
            borrowedBook.setBorrowDate(LocalDate.now());
        }

        // Set new book status to BORROWED
        newBook.setStatus(BookStatus.BORROWED);

        borrowedBook.setBook(newBook);
        borrowedBook.setMember(member);
        // Save oldBook if needed
        if (oldBook != null) {
            bookRepository.save(oldBook);
        }
        bookRepository.save(newBook);
        return borrowedBookRepository.save(borrowedBook);
    }

    public BorrowedBook updateBorrowedBook(Long id, BorrowedBook updatedData) {
        BorrowedBook existing = borrowedBookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowedBook not found"));

        // Validate member
        if (updatedData.getMember() != null && updatedData.getMember().getId() != null) {
            Member member = memberRepository.findById(updatedData.getMember().getId())
                    .orElseThrow(() -> new NotFoundException("Member not found"));
            existing.setMember(member);
        }

        // Validate book
        if (updatedData.getBook() != null && updatedData.getBook().getId() != null) {
            Book newBook = bookRepository.findById(updatedData.getBook().getId())
                    .orElseThrow(() -> new NotFoundException("Book not found"));

            if (!existing.getBook().getId().equals(newBook.getId())) {
                // Revert old book status
                Book oldBook = existing.getBook();
                oldBook.setStatus(BookStatus.AVAILABLE);
                bookRepository.save(oldBook);

                // Check new book availability
                if (newBook.getStatus() != BookStatus.AVAILABLE) {
                    throw new EntityExistException(
                            "Book with id " + newBook.getId() + " is not available for borrowing");
                }

                // Set new book status
                newBook.setStatus(BookStatus.BORROWED);
                existing.setBook(newBook);
                bookRepository.save(newBook);
            }
        }

        // Update dates if provided
        if (updatedData.getBorrowDate() != null) {
            existing.setBorrowDate(updatedData.getBorrowDate());
        }
        if (updatedData.getReturnDate() != null) {
            existing.setReturnDate(updatedData.getReturnDate());
        }

        // Save updated borrowed book (trigger @PreUpdate for updatedAt)
        return borrowedBookRepository.save(existing);
    }

    // Return a borrowed book (set return date and book status to AVAILABLE)
    public BorrowedBook returnBorrowedBook(Long borrowedBookId) {
        BorrowedBook borrowedBook = borrowedBookRepository.findById(borrowedBookId)
                .orElseThrow(() -> new NotFoundException("BorrowedBook with id " + borrowedBookId + " not found"));

        // check if book is already returned
        if (borrowedBook.getReturnDate() != null) {
            throw new ValidationException("Book with id " + borrowedBookId + " has already been returned");
        }
        // Set return date
        borrowedBook.setReturnDate(LocalDate.now());

        // Set book status to AVAILABLE
        Book book = borrowedBook.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);

        return borrowedBookRepository.save(borrowedBook);
    }

    // Get all borrowed books with pagination
    public Page<BorrowedBook> getAllBorrowedBooks(Pageable pageable) {
        return borrowedBookRepository.findAll(pageable);
    }

    // Find borrowed book by id
    public BorrowedBook getBorrowedBookById(Long id) {
        return borrowedBookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("BorrowedBook with id " + id + " not found"));
    }

    // Delete a borrowed book by id
    public void deleteBorrowedBook(Long id) {
        borrowedBookRepository.deleteById(id);
    }

    // Get borrowed books by member id with pagination
    public Page<BorrowedBook> getBorrowedBooksByMemberId(Long memberId, Pageable pageable) {
        return borrowedBookRepository.findByMemberId(memberId, pageable);
    }

    // Get borrowed books by book id with pagination
    public Page<BorrowedBook> getBorrowedBooksByBookId(Long bookId, Pageable pageable) {
        return borrowedBookRepository.findByBookId(bookId, pageable);
    }

    // Search borrowed books by book title or member name
    public Page<BorrowedBook> searchBorrowedBooks(String bookTitle, String memberName, String borrowDate,
            Pageable pageable) {
        java.time.LocalDate date = null;
        if (borrowDate != null && !borrowDate.isBlank()) {
            date = java.time.LocalDate.parse(borrowDate);
        }
        return borrowedBookRepository.findByBookTitleContainingIgnoreCaseOrMemberNameContainingIgnoreCaseOrBorrowDate(
                (bookTitle == null || bookTitle.isBlank()) ? null : bookTitle,
                (memberName == null || memberName.isBlank()) ? null : memberName,
                date,
                pageable);
    }
}
