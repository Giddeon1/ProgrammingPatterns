package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public abstract class User {
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

    public User(int userId, String firstName, String lastName, String email, String phone, String address, LocalDate dateOfBirth,int maxBooksAllowed) {
        this.userId = userId;
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

    public String getFullName() {
        return firstName + " " + lastName;
    }



}
