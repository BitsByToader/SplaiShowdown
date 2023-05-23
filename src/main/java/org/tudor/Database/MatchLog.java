package org.tudor.Database;

/**
 * Data class for storing a match report.
 */
public class MatchLog {
    /** The name of the winner of the match. */
    public String winnerName;
    /** The health left after the match of the winner. */
    public int winnerHP;
    /** The duration of the match in milliseconds. */
    public long matchTime;

    /**
     * Constructor for a MatchLog.
     * @param name Winner name.
     * @param hp Winner remaining health.
     * @param time Match duration.
     */
    public MatchLog(String name, int hp, long time) {
        this.winnerName = name;
        this.winnerHP = hp;
        this.matchTime = time;
    }
}
