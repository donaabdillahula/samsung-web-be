package com.dona_samsung_web_project.samsung_web_be.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "borrowed_books")
public class BorrowedBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

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
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }
    public Member getMember() {
        return member;
    }
    public void setMember(Member member) {
        this.member = member;
    }
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
    public LocalDate getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
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
        return "BorrowedBook{" +
                "id=" + id +
                ", book=" + book.getTitle() +
                ", member=" + member.getName() +
                ", borrowDate=" + borrowDate +
                ", returnDate=" + returnDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }

    // Default constructor
    public BorrowedBook() {}

    // Constructor with parameters
    public BorrowedBook(Book book, Member member, LocalDate borrowDate, LocalDate dueDate) {
        this.book = book;
        this.member = member;
        this.borrowDate = borrowDate;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null; // Not deleted initially
    }
}
