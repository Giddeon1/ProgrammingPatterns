package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public abstract class User {
    protected int userId;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected int maxBooksAllowed;
    protected int overDueBookCount;
    protected List<BorrowedBookRecord> issuedBooks;

    private static int nextId = 1;

    public User(int userId, String firstName, String lastName, String email,int maxBooksAllowed) {
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
        this.overDueBookCount = 0;
        this.issuedBooks = new ArrayList<>();
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
    public abstract List<BorrowedBookRecord> getOverdueRecords();
    public abstract boolean canBorrowBook(Book book);
    public abstract boolean returnBook(Book book);
    public abstract List<Book> searchBook(String keyword);

}
