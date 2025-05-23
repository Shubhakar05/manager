package com.library.manager.controller;

import com.library.manager.dto.TransactionDto;
import com.library.manager.entity.User;
import com.library.manager.service.TransactionService;
import com.library.manager.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepo;

    // Rent a book - only logged-in user can rent for themselves
    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody TransactionDto dto, Authentication authentication) {
        String email = authentication.getName();

        User loggedInUser = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

        TransactionDto createdTransaction = transactionService.createTransaction(dto.getBookId(), loggedInUser);

        return ResponseEntity.ok(createdTransaction);
    }

    // Other methods unchanged...
    @GetMapping
    public List<TransactionDto> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/{id}")
    public TransactionDto getTransaction(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/return/{id}")
    public String returnBookWithoutFine(@PathVariable Long id) {
        transactionService.returnBook(id, null);
        return "Book returned successfully without fine.";
    }

    @PutMapping("/returnWithFine/{id}")
    public String returnBookWithFine(@PathVariable Long id, @RequestParam String customReturnDate) {
        transactionService.returnBook(id, customReturnDate);
        return "Book returned successfully with fine calculated based on delay.";
    }

    @PutMapping("/updateFine/{id}")
    public String updateFine(@PathVariable Long id, @RequestParam Integer extraFine) {
        transactionService.updateFine(id, extraFine);
        return "Fine updated successfully for the user keeping the book.";
    }
}
