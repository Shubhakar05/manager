package com.library.manager.service;

import com.library.manager.dto.TransactionDto;
import com.library.manager.entity.User;

import java.util.List;

public interface TransactionService {
    TransactionDto createTransaction(Long bookId, User user);
    List<TransactionDto> getAllTransactions();
    TransactionDto getTransactionById(Long id);

    void returnBook(Long transactionId, String customReturnDate);
    void calculateFine(Long transactionId);
    void updateFine(Long transactionId, Integer extraFine);
}
