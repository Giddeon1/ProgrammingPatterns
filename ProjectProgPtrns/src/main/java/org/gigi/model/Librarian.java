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
        super(firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return String.format(
                "Librarian Details:%nId: %d%nFirst Name: %s%nLast Name: %s%nEmail: %s",
                userId, firstName, lastName, email);
    }
}
