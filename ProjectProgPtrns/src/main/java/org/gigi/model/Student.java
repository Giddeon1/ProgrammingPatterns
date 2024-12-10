package org.gigi.model;


import lombok.ToString;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
public class Student extends User {
    private static final int MAX_BOOKS_ALLOWED = 3;

    public Student(String firstName, String lastName, String email) {
        super(firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    public Student(int userId, String firstName, String lastName, String email) {
        super(userId, firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    @Override
    public String getDetails() {
        return String.format(
                "Student Details:\nId: %d\nName: %s\nEmail: %s\nOverdue Books: %d\nIssued Books: %d",
                userId, getFullName(), email, getOverdueRecords().size(), issuedBooks.size()
        );
    }


}

