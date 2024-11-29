package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Staff extends User {
    private String password;

    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 5;

    public Staff(String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    public Staff(String firstName, String lastName, String email, String password) {
        super(nextId++, firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    @Override
    public String getDetails() {
        return "Student Details:" + "\nId: " + this.userId +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail: " + email +
                "\nOverdue Books: " + getOverdueRecords() +
                "\nIssued Books: " + issuedBooks.size();
    }

    @Override
    public List<BorrowedBookRecord> getOverdueRecords() {
        List<BorrowedBookRecord> overdueRecords = new ArrayList<>();
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.isOverDue()) {
                overdueRecords.add(record);
            }
        }
        return overdueRecords;
    }



    @Override
    public boolean canBorrowBook(Book book) { //gon look at this more deeply later
        if (issuedBooks.size() <= MAX_BOOKS_ALLOWED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean returnBook(Book book) {
        List<BorrowedBookRecord> borrowedBookRecords = LibrarySystem.getInstance().getBorrowedBookRecords();

        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().equals(book)) {
                record.getBook().incrementAvailableCopies();
                issuedBooks.remove(record);
                borrowedBookRecords.remove(record);

                System.out.println("Book returned successfully: " + record.getBook().getTitle());
                return true;
            }
        }

        System.out.println("No record found for book with ISBN " + book.getTitle());
        return false;
    }

    public boolean returnBook(String bookISBN) {
        List<BorrowedBookRecord> borrowedBookRecords = LibrarySystem.getInstance().getBorrowedBookRecords();
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().getIsbn().equals(bookISBN)) {
                record.getBook().incrementAvailableCopies();
                borrowedBookRecords.remove(record);
                System.out.println("Book returned successfully: " + record.getBook().getTitle());
                return true;
            }
        }
        System.out.println("No record found for book with ISBN " + bookISBN);
        return false;
    }


    @Override
    public List<Book> searchBook(String keyword) {
        List<Book> searchResults = new ArrayList<>();
        List<Book> books = LibrarySystem.getInstance().getBooks();

        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthorFName().toLowerCase().contains(keyword.toLowerCase()) ||
                    book.getAuthorLName().toLowerCase().contains(keyword.toLowerCase())) {
                searchResults.add(book);
            }
        }
        return searchResults;
    }
}
