package org.gigi;

import org.gigi.model.*;

import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    private static LibrarySystem librarySystemInstance;
    private  List<Student> studentList;
    private List<Staff> staffList;
    private List<Book> bookList;
    private List<Librarian> librarianList;
    private List<BorrowedBookRecord> borrowedBookRecordList;

    private LibrarySystem() {
        this.studentList =new ArrayList<>();
        this.staffList = new ArrayList<>();
        this.bookList = new ArrayList<>();
        this.librarianList = new ArrayList<>();
        this.borrowedBookRecordList = new ArrayList<>();
    }

    /**
     * a global point of access to the singleton instance of the LibrarySystem class.
     * @return The singleton instance of the LibrarySystem class.
     */
    public static LibrarySystem getInstance() {
        if (librarySystemInstance == null) {
            synchronized (LibrarySystem.class) {
                if (librarySystemInstance == null) {
                    librarySystemInstance = new LibrarySystem();
                }
            }
        }
        return librarySystemInstance;
    }

    public  User login(int id, String password) {
        for (Student student : studentList) {
            if (student.getUserId() == id) { //check on this later
                return student;
            }
        }
        for (Staff member : staffList) {
            if (member.getUserId() == id) { //check on this later
                return member;
            }
        }
        for (Librarian librarian : librarianList) {
            if (librarian.getUserId() == id) { //check on this later
                return librarian;
            }
        }
        return null;
    }

    public User signUp(String firstName, String lastName, String email, String type) {
        int newId = generateNewId(); // A method to generate a unique ID
        User newUser;

        switch (type.toLowerCase()) {
            case "student":
                newUser = new Student(newId, firstName, lastName, email);
                studentList.add((Student) newUser);
                break;
            case "staff":
                newUser = new Staff(newId, firstName, lastName, email);
                staffList.add((Staff) newUser);
                break;
            case "librarian":
                newUser = new Librarian(newId, firstName, lastName, email);
                librarianList.add((Librarian) newUser);
                break;
            default:
                throw new IllegalArgumentException("Invalid user type");
        }

        return newUser;
    }

    private int generateNewId() {
        return (studentList.size() + staffList.size() + librarianList.size() + 1);
    }
    }


