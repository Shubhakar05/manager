package com.library.manager.controller;

import com.library.manager.dto.BookDto;
import com.library.manager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // Only Admin can add a book
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BookDto addBook(@RequestBody BookDto bookDto) {
        return bookService.addBook(bookDto);
    }

    // Anyone can view all books
    @GetMapping
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    // Anyone can view a book by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookDto> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

    // Only Admin can delete a book by ID
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        return bookService.deleteBook(id);
    }

    // Only Admin can update the available copies of a book
    @PutMapping("/{id}/availableCopies")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookDto> updateAvailableCopies(@PathVariable Long id, @RequestBody int availableCopies) {
        return bookService.updateAvailableCopies(id, availableCopies);
    }
}
