package org.gigi.model;

import lombok.Getter;
import lombok.Setter;
import org.gigi.util.DatabaseUtil;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LibrarySystem {
    private List<Book> books;
    private List<BorrowedBookRecord> borrowedBookRecords;
    private List<User> users;

    private static LibrarySystem librarySystem;

    private LibrarySystem() {
        this.users = DatabaseUtil.fetchAllUsers();
        this.books = DatabaseUtil.fetchAllBooks();
        this.borrowedBookRecords = DatabaseUtil.fetchAllBorrowedBookRecords();
    }

    public BorrowedBookRecord findBorrowedRecord(User user, RegularBook book) {
        return borrowedBookRecords.stream()
                .filter(record -> record.getOwner().equals(user) && record.getBook().equals(book))
                .findFirst()
                .orElse(null);
    }

    /**
     * a global point of access to the singleton instance of the LibrarySystem class.
     * @return The singleton instance of the LibrarySystem class.
     */
    public static LibrarySystem getInstance() {
        if (librarySystem == null) {
            synchronized (LibrarySystem.class) {
                if (librarySystem == null) {
                    librarySystem = new LibrarySystem();
                }
            }
        }
        return librarySystem;
    }


    /**
     * Implement a factory Pattern to determine which type of user it is
     * @param type
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @return
     */
    private User createUser(String type, int id, String firstName, String lastName, String email) {
        switch (type.toLowerCase()) {
            case "student" :
                return new Student(firstName, lastName, email);
            case "librarian" :
                return new Librarian(firstName, lastName, email);
            default:
                throw new IllegalArgumentException("invalid user type");
        }
    }


}


