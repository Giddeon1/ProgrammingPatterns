package org.gigi.model;

import java.util.Date;
import java.util.List;

public abstract class User {
    protected int id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String address;
    protected Date dateOfBirth;
    protected int maxBooksAllowed;
    protected int overdueBooksCount;
    protected List<Book> issuedBooks;

    public User(int id, String firstName, String lastName, String email, String phone, String address, Date dateOfBirth, int maxBooksAllowed, int overdueBooksCount, List<Book> issuedBooks) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.maxBooksAllowed = maxBooksAllowed;
        this.overdueBooksCount = overdueBooksCount;
        this.issuedBooks = issuedBooks;
    }


    public abstract String getDetails();
    public abstract int getAge();
    public abstract List<Book> getOverdueBooks();
    public abstract boolean borrowBook(Book book);
    public abstract boolean returnBook(Book book);
    public abstract boolean canBorrowMoreBooks();
    public abstract List<Book> searchBook(String keyword);
}
