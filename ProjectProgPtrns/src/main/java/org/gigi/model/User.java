package org.gigi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    private static int counter = 0;
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String phone;
    protected String address;
    protected LocalDate dateOfBirth;
    protected int maxBooksAllowed;
    protected int overDueBookCount;
    protected List<Book> issuedBooks;

    public User(String firstName, String lastName, String email, String phone, String address, LocalDate dateOfBirth, int maxBooksAllowed) {
        this.userId = counter++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.maxBooksAllowed = maxBooksAllowed;
        this.overDueBookCount = 0;
        this.issuedBooks = new ArrayList<>();
    }
}
