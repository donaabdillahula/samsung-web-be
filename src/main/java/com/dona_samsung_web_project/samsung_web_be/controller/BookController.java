package com.dona_samsung_web_project.samsung_web_be.controller;

import com.dona_samsung_web_project.samsung_web_be.model.Book;
import com.dona_samsung_web_project.samsung_web_be.model.BookStatus;
import com.dona_samsung_web_project.samsung_web_be.model.response.PagingResponse;
import com.dona_samsung_web_project.samsung_web_be.model.response.SuccessResponse;
import com.dona_samsung_web_project.samsung_web_be.service.BookService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/books")
public class BookController {
        private final BookService bookService;

        public BookController(BookService bookService) {
                this.bookService = bookService;
        }

        // Create a book with photo upload
        @PostMapping(consumes = { "multipart/form-data" })
        public ResponseEntity<SuccessResponse<Book>> createBookWithPhoto(
                        @RequestPart("book") @Valid Book book,
                        @RequestPart(value = "photo", required = false) MultipartFile photo) throws Exception {
                Book savedBook = bookService.saveBook(book, photo);
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(new SuccessResponse<>(savedBook, "Success create book with photo"));
        }

        // Get all books with pagination
        @GetMapping
        public ResponseEntity<PagingResponse<Book>> getAllBooks(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "title") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {
                if (page != -1 && size != -1) {
                        Pageable pageable = PageRequest.of(
                                        page,
                                        size,
                                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                        : Sort.by(sortBy).descending());
                        Page<Book> books = bookService.getAllBooks(pageable);
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new PagingResponse<>(
                                                        books, "Success get book list"));
                } else {
                        Pageable unpaged = Pageable.unpaged(
                                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                        : Sort.by(sortBy).descending());
                        Page<Book> books = bookService.getAllBooks(unpaged);
                        return ResponseEntity.status(HttpStatus.OK)
                                        .body(new PagingResponse<>(
                                                        books, "Success get all book"));
                }
        }

        // Get book by id
        @GetMapping("/{id}")
        public ResponseEntity<SuccessResponse<Book>> getBookById(@PathVariable Long id) {
                Book book = bookService.getBookById(id);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                book, "Success get book with id " + id));
        }

        // Update a book by id
        @PutMapping("/{id}")
        public ResponseEntity<SuccessResponse<Book>> updateBook(
                        @PathVariable Long id,
                        @RequestPart("book") @Valid Book book,
                        @RequestPart(value = "photo", required = false) MultipartFile photo) throws Exception {
                Book updatedBook = bookService.updateBook(id, book, photo);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                updatedBook, "Success update book with id " + id));
        }

        // Delete a book by id
        @DeleteMapping("/{id}")
        public ResponseEntity<SuccessResponse<Void>> deleteBook(@PathVariable Long id) {
                bookService.getBookById(id);
                bookService.deleteBook(id);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new SuccessResponse<>(
                                                null, "Success delete book with id " + id));
        }

        // Search books by title with pagination
        @GetMapping("/search-by-title")
        public ResponseEntity<PagingResponse<Book>> searchBooksByTitle(
                        @RequestParam String title,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "title") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {
                Pageable pageable = PageRequest.of(page, size,
                                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());
                Page<Book> books = bookService.searchBooksByTitle(title, pageable);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(
                                                books, "Success get book list by title"));
        }

        // Search books by category with pagination
        @GetMapping("/search-by-category")
        public ResponseEntity<PagingResponse<Book>> searchBooksByCategory(
                        @RequestParam String category,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "title") String sortBy,
                        @RequestParam(defaultValue = "asc") String sortDir) {
                Pageable pageable = PageRequest.of(page, size,
                                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                                                : Sort.by(sortBy).descending());
                Page<Book> books = bookService.searchBooksByCategory(category, pageable);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(
                                                books, "Success get book list by category"));
        }

        // Get top 3 books based on most borrowed count
        @GetMapping("/top-borrowed")
        public ResponseEntity<PagingResponse<Book>> getTopBooksByBorrowed(
                        @RequestParam(defaultValue = "3") int topN) {
                Page<Book> books = bookService.getTopBooksByBorrowedCount(topN);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(books, "Success get top most borrowed books"));
        }

        @GetMapping("/newest")
        public ResponseEntity<PagingResponse<Book>> getNewestAvailableBooks(
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "AVAILABLE") BookStatus status) {
                Page<Book> books = bookService.getNewestAvailableBooks(page, size, status);
                return ResponseEntity.status(HttpStatus.OK)
                                .body(new PagingResponse<>(books, "Success get newest available books"));
        }
}