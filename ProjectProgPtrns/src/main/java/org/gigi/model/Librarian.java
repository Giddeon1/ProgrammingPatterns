package org.gigi.model;

import lombok.Getter;
import lombok.Setter;
import org.gigi.controller.LibrarySystemController;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Librarian extends User {

    public Librarian (String firstName, String lastName, String email) {
        super(firstName, lastName, email, 0);
    }

    public Librarian (int userId, String firstName, String lastName, String email) {
        super(userId,firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return String.format(
                "Librarian Details:%nId: %d%nFirst Name: %s%nLast Name: %s%nEmail: %s",
                userId, firstName, lastName, email);
    }

    /**
     * Requests adding a book to the library system.
     * @param controller The library system controller.
     * @param book The book to be added.
     */
    public void requestAddBook(LibrarySystemController controller, Book book) {
        controller.addBook(book);
    }

    /**
     * Requests borrowing a book for a user.
     * @param controller The library system controller.
     * @param user The user borrowing the book.
     * @param book The book to be borrowed.
     */
    public void requestBorrowBook(LibrarySystemController controller, User user, RegularBook book) {
        controller.borrowBook(user, book, this);
    }

    /**
     * Requests returning a book for a user.
     * @param controller The library system controller.
     * @param user The user returning the book.
     * @param book The book being returned.
     */
    public void requestReturnBook(LibrarySystemController controller, User user, RegularBook book) {
        controller.returnBook(user, book);
    }
}

