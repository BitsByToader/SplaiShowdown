package org.tudor.Database;

/**
 * Exception class for when a database connection wasn't successful.
 */
public class DatabaseConnectionFailedException extends Exception {
    /**
     * Constructor for this exception type.
     * @param dbName The database that was attempted to open.
     */
    DatabaseConnectionFailedException(String dbName) {
        super("Couldn't connect to database: " + dbName + " :(");
    }
}
