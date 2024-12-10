package org.gigi;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        //DatabaseUtil.DELETE_ALL_TABLES_SQL();
        /*DatabaseUtil.CREATE_BOOK_TABLE_SQL();
        DatabaseUtil.CREATE_STUDENT_TABLE_SQL();
        DatabaseUtil.CREATE_LIBRARIAN_TABLE_SQL();
        DatabaseUtil.CREATE_BORROWED_BOOK_TABLE_SQL();*/

        Student student1 = new Student("John", "Doe", "john.doe@example.com");
        Student student2 = new Student("Jane", "Smith", "jane.smith@example.com");

        System.out.println(student1.getUserId());
        System.out.println(student2.getUserId());


        Librarian librarian1 = new Librarian("Emily", "Brown", "emily.brown@example.com");
        Librarian librarian2 = new Librarian("Chris", "Johnson", "chris.johnson@example.com");
        System.out.println(librarian1.getUserId());
        System.out.println(librarian2.getUserId());

        //DatabaseUtil.insertIntoStudentTable(student2);
        //DatabaseUtil.insertIntoLibrarianTable(librarian2);

        Librarian librarian5 = DatabaseUtil.fetchLibrarianById(4);
        System.out.println(librarian5.getUserId());
        System.out.println("==================================");
        librarian2.setFirstName("nigger");
        System.out.println(librarian2.getFirstName());
        System.out.println(librarian5.getFirstName());




    }
}