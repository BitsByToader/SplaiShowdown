package org.tudor.Database;

public class MatchLog {
    public String winnerName;
    public int winnerHP;
    public long matchTime;

    public MatchLog(String name, int hp, long time) {
        this.winnerName = name;
        this.winnerHP = hp;
        this.matchTime = time;
    }
}
