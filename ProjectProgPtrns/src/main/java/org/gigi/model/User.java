package org.gigi.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
public abstract class User {
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected int maxBooksAllowed;
    protected List<BorrowedBookRecord> issuedBooks;

    private static int nextId = 1;

    public User(String firstName, String lastName, String email,int maxBooksAllowed) {
        if (!isNameValid(firstName, lastName)) {
            throw new IllegalArgumentException("Invalid name: Names must contain only letters and spaces.");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.userId = generateNextId();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.maxBooksAllowed = maxBooksAllowed;
        this.issuedBooks = new ArrayList<>();
    }

    public User(int userId,String firstName, String lastName, String email,int maxBooksAllowed) {
        if (!isNameValid(firstName, lastName)) {
            throw new IllegalArgumentException("Invalid name: Names must contain only letters and spaces.");
        }
        if (!isEmailValid(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.maxBooksAllowed = maxBooksAllowed;
        this.issuedBooks = new ArrayList<>();
    }

    /**
     * method that gives a list of overdue records
     * @return a list of overdue book records
     */
    public List<BorrowedBookRecord> getOverdueRecords() {
        List<BorrowedBookRecord> overdueRecords = new ArrayList<>();
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.isOverDue()) {
                overdueRecords.add(record);
            }
        }
        return overdueRecords;
    }

    /**
     * add a borrowed book record for the user
     * @param record the record to be added
     */
    public void borrowBook(BorrowedBookRecord record) {
        if (issuedBooks.size() >= maxBooksAllowed) {
            throw new IllegalStateException("Maximum books allowed reached. Cannot borrow more books.");
        }
        issuedBooks.add(record);
    }

    /**
     * remove a borrowed book record for the user
     * @param record the record to be removed
     */
    public void returnBook(BorrowedBookRecord record) {
        issuedBooks.remove(record);
    }

    /**
     * Static method to generate the next unique user ID.
     */
    private static synchronized int generateNextId() {
        return nextId++;
    }

    /**
     * concatenate the first and last name of the user
     * @return the user's first name and last name together
     */
    public String getFullName() {
        return firstName+" "+lastName;
    }

    /**
     * method that checks if the name is not valid(has numbers or special charcters)
     * @param firstName the firstName of the User
     * @param lastName the last name of the User
     * @return true or false if the name is valid or not
     */
    private static boolean isNameValid(String firstName, String lastName) {
        String regex = "^[a-zA-Z\\s]+$"; // Only letters and spaces allowed
        return firstName != null && lastName != null && firstName.matches(regex) && lastName.matches(regex);
    }

    /**
     * checks if email is valid, ex: the email has to have @ and a .
     * @param email the string to be checked
     * @return true or false whether the email is valid or not
     */
    private static boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    public abstract String getDetails();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && maxBooksAllowed == user.maxBooksAllowed && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(issuedBooks, user.issuedBooks);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, firstName, lastName, email, maxBooksAllowed, issuedBooks);
    }
}
