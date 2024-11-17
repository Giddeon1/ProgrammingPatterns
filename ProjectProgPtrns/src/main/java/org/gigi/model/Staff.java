package org.gigi.model;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Staff extends User{
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 5;

    public Staff(int userId, String firstName, String lastName, String email, int maxBooksAllowed) {
        super(userId, firstName, lastName, email, maxBooksAllowed);
    }


    @Override
    public String getDetails() {
        return "" ;

    }


    @Override
    public List<Book> getOverdueBooks() {
        return List.of();
    }

    @Override
    public boolean borrowBook(Book book) {
        return false;
    }

    @Override
    public boolean returnBook(Book book) {
        return false;
    }

    @Override
    public boolean canBorrowMoreBooks() {
        return false;
    }

    @Override
    public List<Book> searchBook(String keyword) {
        return List.of();
    }
}
