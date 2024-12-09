package org.gigi.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class BorrowedBookRecord  {
    private final Book book;
    private final User owner;
    private final Librarian issuer;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;


    /**
     * Constructor for BorrowedBookRecord with default borrowing period of 14 days.
     */
    public BorrowedBookRecord(Book book, User owner, Librarian issuer) {
        this(book, owner, issuer, LocalDate.now().plusDays(14)); // Default 14-day period
    }

    /**
     * Constructor for BorrowedBookRecord with custom due date.
     */
    public BorrowedBookRecord(Book book, User owner, Librarian issuer, LocalDate dueDate) {
        if (book == null || owner == null) {
            throw new IllegalArgumentException("Book and owner cannot be null.");
        }
        this.book = book;
        this.owner = owner;
        this.issuer = issuer;
        this.borrowDate = LocalDate.now();
        this.dueDate = dueDate;
        this.returnDate = null;
    }

    /**
     * Marks the book as returned by setting the return date.
     */
    public void returnBook() {
        if (this.returnDate != null) {
            throw new IllegalStateException("This book has already been returned.");
        }
        this.returnDate = LocalDate.now();
    }

    /**
     * Extend the due date by a specified number of days.
     * @param days Number of days to extend.
     */
    public void extendDueDate(int days) {
        if (days <= 0) {
            throw new IllegalArgumentException("Days to extend must be positive.");
        }
        this.dueDate = this.dueDate.plusDays(days);
    }

    /**
     * Checks if the book is overdue.
     * @return true if overdue, otherwise false.
     */
    public boolean isOverDue() {
        return returnDate == null && LocalDate.now().isAfter(dueDate);
    }

    /**
     * Calculates the number of days overdue, or 0 if not overdue.
     * If the book is returned, calculates overdue days relative to the return date.
     * @return number of days overdue.
     */
    public int daysOverDue() {
        if (returnDate != null || !isOverDue()) {
            return 0; // No overdue days if the book has been returned or is not overdue
        }
        return LocalDate.now().compareTo(dueDate);
    }

    @Override
    public String toString() {
        return String.format("BorrowedBookRecord[Book: %s, Borrower: %s, Issuer: %s, Borrowed On: %s, Due Date: %s, Return Date: %s, Overdue: %b]",
                book.getTitle(), owner.getFullName(), issuer != null ? issuer.getFullName() : "N/A",
                borrowDate, dueDate, returnDate != null ? returnDate : "Not Returned", isOverDue());
    }

}
