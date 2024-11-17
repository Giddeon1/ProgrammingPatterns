package org.gigi.model;

import lombok.Getter;
import java.time.LocalDate;
import java.util.List;

@Getter
public class BorrowedBookRecord extends RegularBook {
    @Getter
    private Book book;
    private LocalDate dueDate;
    private LocalDate issueDate;
    private User owner;
    private Librarian librarian;

    public BorrowedBookRecord(String isbn, String title, String authorFName, String authorLName, int year, int copies, List<BorrowedBookRecord> givenBooks, LocalDate dueDate, LocalDate issueDate, User owner, Librarian librarian) {
        super(isbn, title, authorFName, authorLName, year, copies, givenBooks);
        this.dueDate = dueDate;
       this.issueDate = issueDate;
       this.owner = owner;
        this.librarian = librarian;
    }

    public BorrowedBookRecord(Book book, User user) {
        super(); //will neeed to fix this later
    }


    public boolean isOverDue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

}
