package org.tudor.Database;

public class DatabaseConnectionFailedException extends Exception {
    DatabaseConnectionFailedException(String dbName) {
        super("Couldn't connect to database: " + dbName + " :(");
    }
}
