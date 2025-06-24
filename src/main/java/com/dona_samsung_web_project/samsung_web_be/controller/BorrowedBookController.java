package com.dona_samsung_web_project.samsung_web_be.controller;

import com.dona_samsung_web_project.samsung_web_be.model.BorrowedBook;
import com.dona_samsung_web_project.samsung_web_be.model.response.PagingResponse;
import com.dona_samsung_web_project.samsung_web_be.model.response.SuccessResponse;
import com.dona_samsung_web_project.samsung_web_be.service.BorrowedBookService;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/borrowed-books")
public class BorrowedBookController {

        private final BorrowedBookService borrowedBookService;
        private final ModelMapper modelMapper = new ModelMapper();

        public BorrowedBookController(BorrowedBookService borrowedBookService) {
                this.borrowedBookService = borrowedBookService;
        }

        // Create or update a borrowed book
        @PostMapping
        public ResponseEntity<SuccessResponse<BorrowedBook>> createOrUpdateBorrowedBook(
                        @Valid @RequestBody BorrowedBook borrowedBook) {
                modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                BorrowedBook newBorrowedBook = modelMapper.map(borrowedBook, BorrowedBook.class);
                BorrowedBook savedBorrowedBook = borrowedBookService.saveBorrowedBook(newBorrowedBook);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                savedBorrowedBook, "Success create borrowed book"));
        }

        @PutMapping("/return/{id}")
        public ResponseEntity<SuccessResponse<BorrowedBook>> returnBook(@PathVariable Long id) {
                BorrowedBook returned = borrowedBookService.returnBorrowedBook(id);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                returned, "Book returned successfully"));
        }

        // Get all borrowed books with pagination
        @GetMapping
        public ResponseEntity<PagingResponse<BorrowedBook>> getAllBorrowedBooks(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {
                if (page != -1 && size != -1) {
                        Pageable pageable = PageRequest.of(
                                        page,
                                        size,
                                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                        : Sort.by(sortBy).descending());
                        Page<BorrowedBook> borrowedBooks = borrowedBookService.getAllBorrowedBooks(pageable);
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new PagingResponse<>(
                                                        borrowedBooks, "Success get borrowedBook list"));
                } else {
                        Pageable unpaged = Pageable.unpaged(
                                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                        : Sort.by(sortBy).descending());
                        Page<BorrowedBook> borrowedBooks = borrowedBookService.getAllBorrowedBooks(unpaged);
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new PagingResponse<>(
                                                        borrowedBooks, "Success get all borrowedBook"));
                }
        }

        // Get borrowed book by id
        @GetMapping("/{id}")
        public ResponseEntity<SuccessResponse<BorrowedBook>> getBorrowedBookById(@PathVariable Long id) {
                BorrowedBook borrowedBook = borrowedBookService.getBorrowedBookById(id);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                borrowedBook, "Success get borrowed book with id " + id));
        }

        // Update a borrowed book by id
        @PutMapping("/{id}")
        public ResponseEntity<SuccessResponse<BorrowedBook>> updateBorrowedBook(@PathVariable Long id,
                        @Valid @RequestBody BorrowedBook borrowedBook) {
                BorrowedBook updatedBorrowedBook = borrowedBookService.updateBorrowedBook(id, borrowedBook);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                updatedBorrowedBook, "Success update borrowed book with id " + id));
        }

        // Delete a borrowed book by id
        @DeleteMapping("/{id}")
        public ResponseEntity<SuccessResponse<Void>> deleteBorrowedBook(@PathVariable Long id) {
                borrowedBookService.getBorrowedBookById(id);
                borrowedBookService.deleteBorrowedBook(id);
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                .body(new SuccessResponse<>(
                                                null, "Success delete borrowed book with id " + id));
        }

        // Get borrowed books by member id with pagination
        @GetMapping("/member/{memberId}")
        public ResponseEntity<PagingResponse<BorrowedBook>> getBorrowedBooksByMemberId(
                        @PathVariable Long memberId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size) {
                Pageable pageable = PageRequest.of(page, size);
                Page<BorrowedBook> borrowedBooks = borrowedBookService.getBorrowedBooksByMemberId(memberId, pageable);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(
                                                borrowedBooks,
                                                "Success get borrowed book list for member " + memberId));
        }

        // Get borrowed books by book id with pagination
        @GetMapping("/book/{bookId}")
        public ResponseEntity<PagingResponse<BorrowedBook>> getBorrowedBooksByBookId(
                        @PathVariable Long bookId,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {
                Pageable pageable = PageRequest.of(page, size,
                                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());
                Page<BorrowedBook> borrowedBooks = borrowedBookService.getBorrowedBooksByBookId(bookId, pageable);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(
                                                borrowedBooks, "Success get borrowed book list for book " + bookId));
        }

        // Search borrowed books by title or member name
        @GetMapping("/search")
        public ResponseEntity<PagingResponse<BorrowedBook>> searchBorrowedBooks(
                        @RequestParam(required = false) String bookTitle,
                        @RequestParam(required = false) String memberName,
                        @RequestParam(required = false) String borrowDate, // format: yyyy-MM-dd
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "createdAt") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {

                Pageable pageable = PageRequest.of(page, size,
                                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());

                Page<BorrowedBook> borrowedBooks = borrowedBookService.searchBorrowedBooks(
                                bookTitle, memberName, borrowDate, pageable);

                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(
                                                borrowedBooks, "Success search borrowed books"));
        }
}
