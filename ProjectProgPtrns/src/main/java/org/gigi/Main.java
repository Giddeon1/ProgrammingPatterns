package org.gigi;

import org.gigi.controller.LibrarySystemController;
import org.gigi.model.*;
import org.gigi.util.DatabaseUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;



public class Main {
    // Load the ResourceBundle from the properties file
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages.Messages");

    // Method to retrieve the message corresponding to a given key
    public static String getMessage(String key) {
        try {
            return messages.getString(key);
        } catch (java.util.MissingResourceException e) {
            return "Message not found for key: " + key;
        }
    }

    public static void main(String[] args) {

        // Example: Retrieve and display a label and a button text
        System.out.println(getMessage("form.welcomeLabel")); // Output: Welcome
        System.out.println(getMessage("button.addBook"));
        // Output: Add Book
        DatabaseUtil.DELETE_ALL_TABLES_SQL();
    }

    //this did not work something wrong with path
}