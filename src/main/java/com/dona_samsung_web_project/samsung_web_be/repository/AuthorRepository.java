package com.dona_samsung_web_project.samsung_web_be.repository;

import com.dona_samsung_web_project.samsung_web_be.model.Author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    // Custom query method for finding by name (case-insensitive)
    Page<Author> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
