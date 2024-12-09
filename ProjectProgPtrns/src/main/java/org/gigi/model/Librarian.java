package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Librarian extends User {

    public Librarian (String firstName, String lastName, String email) {
        super(firstName, lastName, email, 0);
    }

    @Override
    public String getDetails() {
        return String.format(
                "Librarian Details:%nId: %d%nFirst Name: %s%nLast Name: %s%nEmail: %s",
                userId, firstName, lastName, email);
    }

    /**
     * Adds a book to the library.
     * @param librarySystem The library system to which the book will be added.
     * @param book The book to add.
     */
    public void addBook(LibrarySystem librarySystem, Book book) {
        librarySystem.getBooks().add(book);
    }

    /**
     * Removes a book from the library.
     * @param librarySystem The library system from which the book will be removed.
     * @param book The book to remove.
     */
    public void removeBook(LibrarySystem librarySystem, Book book) {
        librarySystem.getBooks().remove(book);
    }


    /**
     * Facilitates borrowing a book for a user.
     * @param librarySystem The library system.
     * @param user The user borrowing the book.
     * @param book The book being borrowed.
     */
    public void facilitateBorrowing(LibrarySystem librarySystem, User user, RegularBook book) {
        if (book.getAvailableCopies() > 0) {
            BorrowedBookRecord record = new BorrowedBookRecord(book, user, this);
            user.borrowBook(record);
            librarySystem.getBorrowedBookRecords().add(record);
            book.decrementAvailableCopies();
        } else {
            throw new IllegalStateException("No copies of the book are available.");
        }
    }

    /**
     * Facilitates returning a book for a user.
     * @param librarySystem The library system.
     * @param user The user returning the book.
     * @param book The book being returned.
     */
    public void facilitateReturning(LibrarySystem librarySystem, User user, RegularBook book) {
        BorrowedBookRecord record = librarySystem.findBorrowedRecord(user, book);
        if (record == null) {
            throw new IllegalArgumentException("No borrowed record found for this book and user.");
        }
        user.returnBook(record);
        record.returnBook();
        book.incrementAvailableCopies();
    }
}
