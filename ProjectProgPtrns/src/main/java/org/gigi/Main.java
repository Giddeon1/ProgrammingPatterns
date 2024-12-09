package org.gigi;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        RegularBook book = new RegularBook("1234","Kill Me","Gideon","Eleboda",2000,12);


        LibrarySystemController librarySystemController = new LibrarySystemController();

    }
}