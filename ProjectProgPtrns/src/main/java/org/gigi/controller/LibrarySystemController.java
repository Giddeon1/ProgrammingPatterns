package org.gigi.controller;

import org.gigi.model.LibrarySystem;
import org.gigi.model.Student;
import org.gigi.util.DatabaseUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LibrarySystemController {
    private LibrarySystem librarySystem;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(10);

    public LibrarySystemController() {
        this.librarySystem = LibrarySystem.getInstance();
        initTables();
    }


    private void initTables() {
        DatabaseUtil.createStudentTable();
        DatabaseUtil.createBookTable();
        DatabaseUtil.createStaffTable();
        DatabaseUtil.createLibrarianTable();
    }

    public void addStudent(Student student) {
        threadPool.submit(()-> {
            librarySystem.getStudents().add(student);
            DatabaseUtil.insertIntoStudentTable(student);
        });
    }



}
