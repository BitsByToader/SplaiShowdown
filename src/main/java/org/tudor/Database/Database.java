package org.tudor.Database;

import java.sql.*;
import java.util.*;

/**
 * Singleton class that manages the connection and data transfer to the game's SQLite3 database.
 * Currently, it only supports adding match reports to the database and retrieve them.
 */
public class Database {
    /** Singleton object of the class. */
    private static Database singleton = null;

    /** Connection object to the database. */
    Connection connection = null;
    /** Current state of the database connection. */
    DatabaseConnectionState connectionState = DatabaseConnectionState.NOT_CONNECTED;

    /**
     * Getter for the singleton.
     * @return The singleton object.
     */
    public static Database shared() {
        if ( singleton == null ) {
            singleton = new Database();
        }

        return singleton;
    }

    /**
     * Connects to the database of the game, located in the root directory of the game, in the 'game.db' file.
     * @throws DatabaseConnectionFailedException When a connection to the database couldn't be established.
     */
    public void connect() throws DatabaseConnectionFailedException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:game.db");

            Statement stmt = connection.createStatement();
            stmt.execute("""
            CREATE TABLE IF NOT EXISTS ScoreHistory (
                id          integer PRIMARY KEY,
                winner      text    NOT NULL,
                winnerHP    integer  NOT NULL,
                matchTime   integer  NOT NULL
            );
            """);

            connectionState = DatabaseConnectionState.CONNECTED;
        } catch (SQLException e) {
            connectionState = DatabaseConnectionState.NOT_CONNECTED;
            throw new DatabaseConnectionFailedException("game.db");
        }
    }

    /**
     * Closes the connection to the database.
     */
    public void close() {
        if ( connection != null ) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Inserts a new match record in the databaase.
     * @param l The match report to insert.
     * @throws DatabaseNotOpenException If the database isn't opened.
     */
    public void insertMatch(MatchLog l) throws DatabaseNotOpenException {
        if ( connectionState == DatabaseConnectionState.NOT_CONNECTED ) {
            throw new DatabaseNotOpenException();
        }

        try {
            PreparedStatement pstmt = connection.prepareStatement("""
            INSERT INTO ScoreHistory
                (winner, winnerHP, matchTime)
            VALUES
                (?, ?, ?);
            """);

            pstmt.setString(1, l.winnerName);
            pstmt.setInt(2, l.winnerHP);
            pstmt.setLong(3, l.matchTime);

            pstmt.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all the previous matches stored in the database.
     * @return A list of the matches.
     * @throws DatabaseNotOpenException If the database isn't open.
     */
    public List<MatchLog> getMatches() throws DatabaseNotOpenException {
        if ( connectionState == DatabaseConnectionState.NOT_CONNECTED ) {
            throw new DatabaseNotOpenException();
        }

        List<MatchLog> results = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("""
                SELECT * FROM ScoreHistory;
            """);

            while ( rs.next() ) {
                results.add(new MatchLog(
                        rs.getString("winner"),
                        rs.getInt("winnerHp"),
                        rs.getLong("matchTime")
                ));
            }

        } catch ( SQLException e ) {
            e.printStackTrace();
        }

        return results;
    }
}
