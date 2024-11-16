package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Book {
    protected String isbn;
    protected String title;
    protected String authorFName;
    protected String authorLName;
    protected int year;
    protected int copies;

    public Book(String isbn, String title, String authorFName, String authorLName, int year, int copies) {
        this.isbn = isbn;
        this.title = title;
        this.authorFName = authorFName;
        this.authorLName = authorLName;
        this.year = year;
        this.copies = copies;
    }


}
