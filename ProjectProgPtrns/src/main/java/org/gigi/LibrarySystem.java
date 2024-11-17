package org.gigi;

import org.gigi.model.*;

import java.util.ArrayList;
import java.util.List;

public class LibrarySystem {
    private  List<Student> studentList = new ArrayList<>();
    private List<Staff> staffList = new ArrayList<>();
    private List<Book> bookList = new ArrayList<>();
    private List<Librarian> librarianList = new ArrayList<>();
    private List<BorrowedBookRecord> borrowedBookRecordList = new ArrayList<>();



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


