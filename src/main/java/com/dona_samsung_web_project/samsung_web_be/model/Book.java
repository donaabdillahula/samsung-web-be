package com.dona_samsung_web_project.samsung_web_be.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 250)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "category", nullable = false, length = 100)
    private String category;

    @Column(name = "publishing_year")
    private Integer publishingYear;

    @Column(name = "photo_path", length = 500)
    private String photoPath;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookStatus status = BookStatus.AVAILABLE;

    @Column(name = "createdAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Integer getPublishingYear() {
        return publishingYear;
    }
    public void setPublishingYear(Integer publishingYear) {
        this.publishingYear = publishingYear;
    }
    public String getPhotoPath() {
        return photoPath;
    }
    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
    public Author getAuthor() {
        return author;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    public BookStatus getStatus() {
        return status;
    }
    public void setStatus(BookStatus status) {
        this.status = status;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    // Lifecycle callback to set createdAt on persist
    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    // Lifecycle callback to set updatedAt on update
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Lifecycle callback to set deletedAt on delete
    @PreRemove
    protected void onDelete() {
        deletedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", publishingYear=" + publishingYear +
                ", photoPath='" + photoPath + '\'' +
                ", author=" + author.getName() +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

    // Default constructor
    public Book() {}

    // Constructor with parameters
    public Book(String title, String description, String category, Integer publishingYear, String photoPath, Author author) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.publishingYear = publishingYear;
        this.photoPath = photoPath;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null; // Not deleted initially
    }
}