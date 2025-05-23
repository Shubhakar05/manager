package com.library.manager.service;

import com.library.manager.dto.TransactionDto;
import com.library.manager.entity.Book;
import com.library.manager.entity.Transaction;
import com.library.manager.entity.User;
import com.library.manager.entity.Role;
import com.library.manager.repo.BookRepository;
import com.library.manager.repo.TransactionRepository;
import com.library.manager.repo.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private UserRepository userRepo;

    @Override
    public TransactionDto createTransaction(Long bookId, User user) {
        Book book = bookRepo.findById(bookId).orElseThrow();

        Transaction transaction = new Transaction();
        transaction.setBook(book);
        transaction.setUser(user);
        transaction.setIssueDate(LocalDate.now());
        transaction.setExpectedReturnDate(transaction.getIssueDate().plusDays(14)); // 14-day rental
        transaction.setReturned(false);

        Transaction saved = transactionRepo.save(transaction);

        TransactionDto dto = new TransactionDto();
        dto.setId(saved.getId());
        dto.setBookId(saved.getBook().getId());
        dto.setUserId(saved.getUser().getId());
        dto.setIssueDate(saved.getIssueDate());
        dto.setExpectedReturnDate(saved.getExpectedReturnDate());

        return dto;
    }

    // Other methods remain unchanged

    @Override
    public List<TransactionDto> getAllTransactions() {
        return transactionRepo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        Transaction transaction = transactionRepo.findById(id).orElseThrow();
        return convertToDto(transaction);
    }

    @Override
    public void returnBook(Long transactionId, String customReturnDate) {
        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
        transaction.setReturned(true);

        LocalDate returnDate = customReturnDate != null
                ? LocalDate.parse(customReturnDate, DateTimeFormatter.ISO_DATE)
                : LocalDate.now();

        transaction.setActualReturnDate(returnDate);
        calculateFine(transaction);
        transactionRepo.save(transaction);
    }

    @Override
    public void calculateFine(Long transactionId) {
        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
        calculateFine(transaction);
    }

    public void calculateFine(Transaction transaction) {
        User user = transaction.getUser();
        Role role = user.getRole();

        if (role == Role.ADMIN || role == Role.TEACHER) {
            transaction.setFineAmount(0);
            transaction.setReturned(true);
            transaction.setBookKept(false);
        } else {
            LocalDate expectedReturnDate = transaction.getExpectedReturnDate();
            LocalDate actualReturnDate = transaction.getActualReturnDate();

            long delayDays = ChronoUnit.DAYS.between(expectedReturnDate, actualReturnDate);

            if (delayDays > 0) {
                if (delayDays <= 25) {
                    transaction.setFineAmount((int) delayDays * 5);
                    transaction.setReturned(true);
                    transaction.setBookKept(false);
                } else {
                    transaction.setFineAmount(125);
                    transaction.setBookKept(true);
                    transaction.setReturned(false);
                }
            } else {
                transaction.setFineAmount(0);
            }
        }

        transactionRepo.save(transaction);
    }

    @Override
    public void updateFine(Long transactionId, Integer extraFine) {
        Transaction transaction = transactionRepo.findById(transactionId).orElseThrow();
        int updatedFine = transaction.getFineAmount() + extraFine;
        transaction.setFineAmount(updatedFine);
        transaction.setBookKept(true);
        transactionRepo.save(transaction);
    }

    private TransactionDto convertToDto(Transaction t) {
        TransactionDto dto = new TransactionDto();
        dto.setId(t.getId());
        dto.setBookId(t.getBook().getId());
        dto.setUserId(t.getUser().getId());
        dto.setIssueDate(t.getIssueDate());
        dto.setExpectedReturnDate(t.getExpectedReturnDate());
        dto.setActualReturnDate(t.getActualReturnDate());
        dto.setFineAmount(t.getFineAmount());
        dto.setBookKept(t.isBookKept());
        dto.setReturned(t.isReturned());
        return dto;
    }
}
