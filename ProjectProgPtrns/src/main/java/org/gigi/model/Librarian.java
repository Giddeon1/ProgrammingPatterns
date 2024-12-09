package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Librarian extends User {
    private static int nextId = 1;
    //private static final int MAX_BOOKS_ALLOWED = 5;

    public Librarian (String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return "Librarian details:" +
                "\nId: " + this.userId +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail:" + email;
    }

    @Override
    public List<BorrowedBookRecord> getOverdueRecords() {
        return List.of();
    }

    @Override
    public boolean canBorrowBook(Book book) {
        return false;
    }

    @Override
    public boolean returnBook(Book book) {
        return false;
    }

    @Override
    public List<Book> searchBook(String keyword) {
        return List.of();
    }


}
