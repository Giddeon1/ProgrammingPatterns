package org.gigi.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public abstract class Book {
    protected String isbn;
    protected String title;
    protected String authorFName;
    protected String authorLName;
    protected int year;
    protected int totalCopies;
    @Getter
    protected int availableCopies;
    protected boolean isAvailable;



    public Book(String isbn, String title, String authorFName, String authorLName, int year, int totalCopies) {
        if (!isNameValid(authorFName, authorLName)) {
            throw new IllegalArgumentException("Invalid name: Names must contain only letters and spaces.");
        }
        if (!isValidYear(year)) {
            throw new IllegalArgumentException("Invalid Year, the published year should be less than or equals to current year");
        }
        this.isbn = isbn;
        this.title = title;
        this.authorFName = authorFName;
        this.authorLName = authorLName;
        this.year = year;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.isAvailable = true;

    }

    public void setAvailableCopies(int availableCopies) {
        if (availableCopies <= totalCopies && availableCopies >= 0) {
            this.availableCopies = availableCopies;
        }
    }

    public void incrementAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }

    public void decrementAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }





    /**
     * method that checks if the name is not valid(has numbers or special charcters)
     * @param firstName the firstName of the author
     * @param lastName the last name of the author
     * @return true or false if the name is valid or not
     */
    private static boolean isNameValid(String firstName, String lastName) {
        String regex = "^[a-zA-Z\\s]+$"; // Only letters and spaces allowed
        return firstName != null && lastName != null && firstName.matches(regex) && lastName.matches(regex);
    }

    /**
     * check whether the publishing year is either less than the current year or past the current year
     * @param yearPublished the year the book was published
     * @return true if the published year is <= current year or false if not
     */
    private static boolean isValidYear(int yearPublished) {
        return yearPublished <= LocalDate.now().getYear();
    }
}

