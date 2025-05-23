package com.library.manager.service;

import com.library.manager.dto.BookDto;
import com.library.manager.entity.Book;
import com.library.manager.repo.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public BookDto addBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setAuthor(bookDto.getAuthor());
        book.setAvailableCopies(bookDto.getAvailableCopies());

        Book savedBook = bookRepository.save(book);
        return mapToDto(savedBook);
    }

    private BookDto mapToDto(Book book) {
        BookDto dto = new BookDto();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setAvailableCopies(book.getAvailableCopies());
        return dto;
    }

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ResponseEntity<BookDto> getBookById(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Book not found, return 404
        }
        return ResponseEntity.ok(mapToDto(bookOptional.get()));
    }

    public ResponseEntity<String> deleteBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book not found with ID: " + id);  // Book not found
        }
        bookRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book deleted successfully");  // 204 for successful deletion
    }

    public ResponseEntity<BookDto> updateAvailableCopies(Long id, int availableCopies) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);  // Book not found, return 404
        }

        Book book = bookOptional.get();
        book.setAvailableCopies(availableCopies);
        Book updatedBook = bookRepository.save(book);
        return ResponseEntity.ok(mapToDto(updatedBook));
    }
}
