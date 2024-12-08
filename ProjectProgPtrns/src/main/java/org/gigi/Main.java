package org.gigi;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        RegularBook book = new RegularBook("1234","Kill Me","Gideon","Eleboda",2000,12);
        Student me = new Student(1,"Gidoen","eleboda","deon@gmail.com");
        Librarian lib1 = new Librarian(1,"itunu","eleboda","me@gmail.com");
        BorrowedBookRecord borrowedBookRecord = new BorrowedBookRecord(book,me,lib1);
        System.out.println(borrowedBookRecord.getOwner().getFirstName());
        book.getGivenBooks().add(borrowedBookRecord);
        System.out.println(book.getGivenBooks());
    }
}