package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BorrowedBookRecord  {
    private Book book;
    private LocalDate dueDate;
    private LocalDate issueDate;
    private User owner;
    private Librarian librarian;

    public BorrowedBookRecord(Book book, LocalDate dueDate, LocalDate issueDate, User owner, Librarian librarian) {
        if (book == null || owner == null || librarian == null) {
            throw new IllegalArgumentException("Book, owner, and librarian cannot be null.");
        }

        this.book = book;
        this.dueDate = dueDate;
        this.issueDate = issueDate;
        this.owner = owner;
        this.librarian = librarian;
    }

    public BorrowedBookRecord(Book book, User owner) {
        if (book == null || owner == null) {
            throw new IllegalArgumentException("Book and owner cannot be null");
        }
        this.book = book;
        this.owner = owner;
        this.issueDate = LocalDate.now();
        this.dueDate = issueDate.plusWeeks(2);
        this.librarian = null;
    }


    public boolean isOverDue() {
        return LocalDate.now().isAfter(dueDate);
    }

}
