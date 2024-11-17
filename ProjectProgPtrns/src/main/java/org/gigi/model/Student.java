package org.gigi.model;

import java.time.LocalDate;
import java.util.List;

public class Student extends User {
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 3;

    public Student(String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    @Override
    public String getDetails() {
        return "";
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
    public List<Book> searchBook(String keyword) {
        return List.of();
    }
}
