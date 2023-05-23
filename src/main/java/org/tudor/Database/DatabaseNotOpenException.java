package org.tudor.Database;

/**
 * Exception class for when a database operation is requested but the database isn't open.
 */
public class DatabaseNotOpenException extends Exception {
    /**
     * Constructor for the exception.
     */
    public DatabaseNotOpenException() {
        super("SQLite database not open!");
    }
}
