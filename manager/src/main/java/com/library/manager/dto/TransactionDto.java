package com.library.manager.dto;

import java.time.LocalDate;

public class TransactionDto {
    private Long id;
    private Long bookId;
    private Long userId;
    private LocalDate issueDate;
    private LocalDate expectedReturnDate;
    private LocalDate actualReturnDate;
    private int fineAmount;
    private boolean isBookKept;
    private boolean returned;

  
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDate expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public int getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(int fineAmount) {
        this.fineAmount = fineAmount;
    }

    public boolean isBookKept() {
        return isBookKept;
    }

    public void setBookKept(boolean bookKept) {
        isBookKept = bookKept;
    }

    public boolean isReturned() {
        return returned;
    }

    public void setReturned(boolean returned) {
        this.returned = returned;
    }

	public void setReturnDate(Object returnDate) {
		// TODO Auto-generated method stub
		
	}
}
