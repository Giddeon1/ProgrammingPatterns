package org.gigi.model;

import lombok.Getter;
import java.time.LocalDate;
@Getter
public class BorrowedBookRecord {
    private LocalDate dueDate;
    private LocalDate issueDate;
    private User owner;
    private Librarian librarian;

    public BorrowedBookRecord(LocalDate dueDate, LocalDate issueDate, User owner, Librarian librarian) {
        this.dueDate = dueDate;
        this.issueDate = issueDate;
        this.owner = owner;
        this.librarian = librarian;
    }

    public boolean isOverDue() {
        return LocalDate.now().isAfter(dueDate);
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }


}
