package com.dona_samsung_web_project.samsung_web_be.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Table(name = "authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 250)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
        createdAt  = updatedAt = LocalDateTime.now();
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
        return "Author{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deletedAt=" + deletedAt +
                '}';
    }
    
    // Default constructor
    public Author() {}

    // Constructor with name and description parameters
    public Author(String name, String description) {
        this.name = name;
        this.description = description;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.deletedAt = null; // Not deleted initially 
    }
}
