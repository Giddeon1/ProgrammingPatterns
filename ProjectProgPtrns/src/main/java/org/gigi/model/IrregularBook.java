package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class IrregularBook extends Book{

    public IrregularBook(String isbn, String title, String authorFName, String authorLName, int year, int copies) {
        super(isbn, title, authorFName, authorLName, year, copies);
    }

}
