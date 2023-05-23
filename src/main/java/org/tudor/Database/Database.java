package org.tudor.Database;

import java.sql.*;
import java.util.*;

public class Database {
    private static Database singleton = null;

    Connection connection = null;
    DatabaseConnectionState connectionState = DatabaseConnectionState.NOT_CONNECTED;

    public static Database shared() {
        if ( singleton == null ) {
            singleton = new Database();
        }

        return singleton;
    }

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

    public void close() {
        if ( connection != null ) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

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
