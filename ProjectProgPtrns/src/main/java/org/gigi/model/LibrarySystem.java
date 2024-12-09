package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

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
        this.users = new ArrayList<>();
        this.books = new ArrayList<>();
        this.borrowedBookRecords = new ArrayList<>();
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

//    public  User login(int id, String password) {
//        for (Student student : students) {
//            if (student.getUserId() == id && student.getPassword().equals(password)) { //check on this later
//                return student;
//            }
//        }
//        for (Staff staff : staffs) {
//            if (staff.getUserId() == id && staff.getPassword().equals(password)) { //check on this later
//                return staff;
//            }
//        }
//        return null;
//    }


    private User createUser(String type, int id, String firstName, String lastName, String email) {
        switch (type.toLowerCase()) {
            case "student" :
                return new Student(firstName, lastName, email);
            case "staff" :
                return new Staff(firstName, lastName, email);
            case "librarian" :
                return new Librarian(firstName, lastName, email);
            default:
                throw new IllegalArgumentException("invalid user type");
        }
    }


//    public User signUp(String firstName, String lastName, String email, String password, String type) {
//        int newId = generateNewId(); // A method to generate a unique ID
//        User newUser = createUser(type, newId, firstName, lastName, email);
//
//        if (newUser instanceof Student) {
//            students.add((Student) newUser);
//        } else if (newUser instanceof Staff) {
//            staffs.add((Staff) newUser);
//        } else if (newUser instanceof Librarian) {
//            System.out.println("Librarian cannot sign up");
//        }
//        return newUser;
//    }
}


