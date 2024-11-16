package org.gigi.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class IrregularBook extends Book{
    List<InLibraryUseRecord> givenBooks;

    public IrregularBook(String isbn, String title, String authorFName, String authorLName, int year, int copies) {
        super(isbn, title, authorFName, authorLName, year, copies);
        this.givenBooks = new ArrayList<>();
    }
}
