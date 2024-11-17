package org.gigi.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    private static int nextId = 1;
    private static final int MAX_BOOKS_ALLOWED = 3;

    public Student(int id, String firstName, String lastName, String email) {
        super(nextId++, firstName, lastName, email, MAX_BOOKS_ALLOWED);
    }

    @Override
    public String getDetails() {
        return "Student details:" +
                "\nId: " + this.userId +
                "\nFirst Name: " + firstName +
                "\nLast Name: " + lastName +
                "\nEmail:" + email +
                "\nAmount of overdue books" + overDueBookCount +
                "\nAmount of books owned" + issuedBooks;
    }

    //will need to override this
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
    public List<Book> getOverdueBooks() {
//        List<Book> overdueBooks = new ArrayList<>();
//        for (BorrowedBookRecord record : getOverdueRecords()) {
//          overdueBooks.add(record.getBook());
//        }
        return List.of();
    }

    @Override
    public boolean canBorrowBook(Book book) {
        if (issuedBooks.size() <= MAX_BOOKS_ALLOWED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean returnBook(Book book) {
        for (BorrowedBookRecord record : issuedBooks) {
            if (record.getBook().equals(book)) {
                issuedBooks.remove(record);
                book.incrementAvailableCopies();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Book> searchBook(String keyword) {
//        List<Book> results = new ArrayList<>();
//        for (Book book : availableBooks) { // Assume availableBooks is a List<Book> but there is an error and i need to fix it later
//            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getAuthorFName().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getAuthorLName().toLowerCase().contains(keyword.toLowerCase()) ||
//                    book.getIsbn().contains(keyword)) {
//                results.add(book);
//            }
//        }
//        return results;
        return null;
    }
}

