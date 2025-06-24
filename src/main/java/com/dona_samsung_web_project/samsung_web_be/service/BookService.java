package com.dona_samsung_web_project.samsung_web_be.service;

import com.dona_samsung_web_project.samsung_web_be.exception.NotFoundException;
import com.dona_samsung_web_project.samsung_web_be.model.Author;
import com.dona_samsung_web_project.samsung_web_be.model.Book;
import com.dona_samsung_web_project.samsung_web_be.model.BookStatus;
import com.dona_samsung_web_project.samsung_web_be.repository.AuthorRepository;
import com.dona_samsung_web_project.samsung_web_be.repository.BookRepository;
import com.dona_samsung_web_project.samsung_web_be.storage.ImageKitStorageService;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ImageKitStorageService imageKitStorageService;
    private final String imageKitFolder;

    public BookService(BookRepository bookRepository,
            AuthorRepository authorRepository,
            ImageKitStorageService imageKitStorageService,
            @Value("samsung-web") String imageKitFolder) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.imageKitStorageService = imageKitStorageService;
        this.imageKitFolder = imageKitFolder;
    }

    // Save a book (create or update)
    public Book saveBook(Book book, MultipartFile photo) throws Exception {
        // Validate author
        if (book.getAuthor() == null || book.getAuthor().getId() == null) {
            throw new NotFoundException("Author is required");
        }
        Author author = authorRepository.findById(book.getAuthor().getId())
                .orElseThrow(() -> new NotFoundException("Author not found"));
        book.setAuthor(author);

        // Handle photo upload/update
        if (photo != null && !photo.isEmpty()) {
            // Validate file is image
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Uploaded file must be an image");
            }

            // If updating, delete previous photo from ImageKit
            if (book.getId() != null) {
                Book existingBook = bookRepository.findById(book.getId())
                        .orElseThrow(() -> new NotFoundException("Book not found"));
                String oldPhotoPath = existingBook.getPhotoPath();
                String oldFileId = extractImageKitFileIdFromUrl(oldPhotoPath);
                if (oldFileId != null) {
                    try {
                        imageKitStorageService.deleteFile(oldFileId);
                    } catch (Exception ignored) {
                    }
                }
            }
            // Add unique prefix to filename
            String uniqueFileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            String photoUrl = imageKitStorageService.uploadFile(photo, uniqueFileName, imageKitFolder);
            book.setPhotoPath(photoUrl);
        }

        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book updatedData, MultipartFile photo) throws Exception {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        existingBook.setTitle(updatedData.getTitle());
        existingBook.setDescription(updatedData.getDescription());
        existingBook.setCategory(updatedData.getCategory());
        existingBook.setPublishingYear(updatedData.getPublishingYear());
        existingBook.setStatus(updatedData.getStatus());

        // Update author
        if (updatedData.getAuthor() != null && updatedData.getAuthor().getId() != null) {
            Author author = authorRepository.findById(updatedData.getAuthor().getId())
                    .orElseThrow(() -> new NotFoundException("Author not found"));
            existingBook.setAuthor(author);
        }

        // Handle photo
        if (photo != null && !photo.isEmpty()) {
            // Validate file is image
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Uploaded file must be an image");
            }

            // Delete old photo
            String oldPhotoPath = existingBook.getPhotoPath();
            String oldFileId = extractImageKitFileIdFromUrl(oldPhotoPath);
            if (oldFileId != null) {
                try {
                    imageKitStorageService.deleteFile(oldFileId);
                } catch (Exception ignored) {
                }
            }

            // Upload new photo
            String uniqueFileName = UUID.randomUUID() + "_" + photo.getOriginalFilename();
            String photoUrl = imageKitStorageService.uploadFile(photo, uniqueFileName, imageKitFolder);
            existingBook.setPhotoPath(photoUrl);
        }

        return bookRepository.save(existingBook);
    }

    // Get all books with pagination
    public Page<Book> getAllBooks(@NotNull Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    // Find book by id
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
    }

    // Delete a book by id and its photo from ImageKit
    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));
        String oldFileId = extractImageKitFileIdFromUrl(book.getPhotoPath());
        if (oldFileId != null) {
            try {
                imageKitStorageService.deleteFile(oldFileId);
            } catch (Exception ignored) {
            }
        }
        bookRepository.deleteById(id);
    }

    // Search books by title with pagination
    public Page<Book> searchBooksByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    // Search books by category with pagination
    public Page<Book> searchBooksByCategory(String category, Pageable pageable) {
        return bookRepository.findByCategoryContainingIgnoreCase(category, pageable);
    }

    public Page<Book> getTopBooksByBorrowedCount(int topN) {
        Pageable pageable = PageRequest.of(0, topN);
        return bookRepository.findTopBooksByBorrowedCount(pageable);
    }

    public Page<Book> getNewestAvailableBooks(int page, int size, BookStatus status) {
        Pageable pageable = PageRequest.of(page, size);
        return bookRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
    }

    // Helper to extract ImageKit fileId from URL
    private String extractImageKitFileIdFromUrl(String url) {
        if (url == null)
            return null;
        // Example: https://ik.imagekit.io/your_id/books/uuid_filename.jpg
        try {
            Pattern pattern = Pattern.compile("/([^/?#]+)$");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception ignored) {
        }
        return null;
    }
}