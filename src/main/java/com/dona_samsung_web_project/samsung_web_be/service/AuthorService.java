package com.dona_samsung_web_project.samsung_web_be.service;

import com.dona_samsung_web_project.samsung_web_be.exception.NotFoundException;
import com.dona_samsung_web_project.samsung_web_be.model.Author;
import com.dona_samsung_web_project.samsung_web_be.repository.AuthorRepository;

import jakarta.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // Save an author
    public Author saveAuthor(@NotNull Author author) {
        return authorRepository.save(author);
    }


    public Author updateAuthor(@NotNull Long id, Author updatedData) {
        Author existing = getAuthorById(id);
        existing.setName(updatedData.getName());
        existing.setDescription(updatedData.getDescription());
        return authorRepository.save(existing);
    }
   
    // Get all authors with pagination
    public Page<Author> getAllAuthors(@NotNull Pageable pageable) {
        return authorRepository.findAll(pageable);
    }

    // Find author by id
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Author with id " + id + " not found"));
    }

    // Delete an author by id
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }

    // Search authors by name with pagination
    public Page<Author> searchAuthorsByName(@NotNull String name, @NotNull Pageable pageable) {
        return authorRepository.findByNameContainingIgnoreCase(name, pageable);
    }
}
