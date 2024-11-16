package org.gigi.model;

import java.time.LocalDate;

public class Student extends User {
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 3;

    public Student(String firstName, String lastName, String email, String phone, String address, LocalDate dateOfBirth) {
        super(nextId++, firstName, lastName, email, phone, address, dateOfBirth, MAX_BOOKS_ALLOWED);
    }




}
