package com.dona_samsung_web_project.samsung_web_be.controller;

import com.dona_samsung_web_project.samsung_web_be.model.Author;
import com.dona_samsung_web_project.samsung_web_be.model.response.PagingResponse;
import com.dona_samsung_web_project.samsung_web_be.model.response.SuccessResponse;
import com.dona_samsung_web_project.samsung_web_be.service.AuthorService;

import org.modelmapper.ModelMapper;

import jakarta.validation.Valid;

import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final ModelMapper modelMapper = new ModelMapper();

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // Create or update an author
    @PostMapping
    public ResponseEntity<SuccessResponse<Author>> createOrUpdateAuthor(@Valid @RequestBody Author author)
            throws Exception {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Author newAuthor = modelMapper.map(author, Author.class);
        Author savedAuthor = authorService.saveAuthor(newAuthor);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        savedAuthor, "Success create author"));
    }

    // Get all authors with pagination
    @GetMapping
    public ResponseEntity<PagingResponse<Author>> getAllAuthors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        if (page != -1 && size != -1) {
            Pageable pageable = PageRequest.of(page, size,
                    sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            Page<Author> authors = authorService.getAllAuthors(pageable);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PagingResponse<>(
                            authors, "Success get author list"));
        } else {
            Pageable unpaged = Pageable.unpaged(
                    sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
            Page<Author> authors = authorService.getAllAuthors(unpaged);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new PagingResponse<>(
                            authors, "Success get all author"));
        }
    }

    // Get author by id
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<Author>> getAuthorById(@PathVariable Long id) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        author, "Success get author with id " + id));
    }

    // Update an author by id
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<Author>> updateAuthor(@PathVariable Long id,
            @Valid @RequestBody Author author) {
        Author updatedAuthor = authorService.updateAuthor(id, author);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessResponse<>(
                        updatedAuthor, "Success update author with id " + id));
    }

    // Delete an author by id
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<Void>> deleteAuthor(@PathVariable Long id) {
        authorService.getAuthorById(id);
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new SuccessResponse<>(
                        null, "Success delete author with id " + id));
    }

    // Search authors by name with pagination
    @GetMapping("/search")
    public ResponseEntity<PagingResponse<Author>> searchAuthorsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending());
        Page<Author> authors = authorService.searchAuthorsByName(name, pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new PagingResponse<>(
                        authors, "Success get author list"));
    }
}
