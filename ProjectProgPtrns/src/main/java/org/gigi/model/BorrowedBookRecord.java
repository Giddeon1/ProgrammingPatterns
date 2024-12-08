package org.gigi.model;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
public class BorrowedBookRecord  {
    @Getter
    private RegularBook book;
    private LocalDate dueDate;
    private LocalDate issueDate;
    private User owner;
    private Librarian librarian;

    public BorrowedBookRecord(RegularBook book,User owner,Librarian librarian) {
        this.book = book;
        this.owner = owner;
        this.dueDate = LocalDate.now().plusWeeks(2);
        this.issueDate = LocalDate.now();
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

    @Override
    public String toString() {
        return "BorrowedBookRecord{" +
                "book=" + book.getTitle() +
                ", bookId=" + book.isbn +
                ", issueDate=" + issueDate +
                ", dueDate=" + dueDate +
                ", ownerName=" + owner.getFullName() +
                ", ownerId=" + owner.userId +
                ", librarianName=" + librarian.getFullName() +
                ", librarianId=" + librarian.userId +
        '}';
    }
}
