package org.gigi.model;

import javax.xml.crypto.Data;
import java.util.Date;

public class BorrowedBookRecord {
    private Date dueDate;
    private Date issueDate;
    private boolean isOverDue;
    private User owner;
    private Librarian librarian;
}
