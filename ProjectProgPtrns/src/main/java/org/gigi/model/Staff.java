package org.gigi.model;

import java.time.LocalDate;

public class Staff extends User{
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 5;

    public Staff(String firstName, String lastName, String email) {
        super(nextId++,firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }
}
