package org.tudor.Database;

public class DatabaseNotOpenException extends Exception {
    public DatabaseNotOpenException() {
        super("SQLite database not open!");
    }
}
