package org.gigi;

import org.gigi.model.Book;
import org.gigi.model.BorrowedBookRecord;
import org.gigi.model.RegularBook;
import org.gigi.model.User;

import java.time.LocalDate;
import java.util.Date;

public class Main {
    public static void main(String[] args) {


        RegularBook book = new RegularBook("8900","dog cat",
                "Gideon", "Eleboda",2000,20);

        BorrowedBookRecord b1 = new BorrowedBookRecord(LocalDate.of(2024,11,15),
                LocalDate.of(2024,11,19),)









    }
}