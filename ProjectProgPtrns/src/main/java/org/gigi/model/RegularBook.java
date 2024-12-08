package org.gigi.model;


import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class RegularBook extends Book {
    private List<BorrowedBookRecord> givenBooks;

    public RegularBook(String isbn, String title, String authorFName, String authorLName, int year, int copies) {
        super(isbn, title, authorFName, authorLName, year, copies);
        this.givenBooks = new ArrayList<>();
    }

}
