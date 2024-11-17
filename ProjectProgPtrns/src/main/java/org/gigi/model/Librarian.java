package org.gigi.model;

public class Librarian extends User {
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 5;

    public Librarian (String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, , , MAX_BOOKS_ALLOWED);
    }




}
