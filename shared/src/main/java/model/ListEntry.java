package model;

import java.util.Objects;

public class ListEntry {
    public Integer gameID;
    public String gameName;
    public String whiteUsername;
    public String blackUsername;

    public ListEntry(GameData gameData){
        gameID = gameData.getGameID();;
        gameName = gameData.getGameName();
        whiteUsername = gameData.getWhiteUsername();
        blackUsername = gameData.getBlackUsername();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ListEntry listEntry = (ListEntry) o;
        return Objects.equals(gameID, listEntry.gameID) && Objects.equals(gameName, listEntry.gameName) && Objects.equals(whiteUsername, listEntry.whiteUsername) && Objects.equals(blackUsername, listEntry.blackUsername);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName, whiteUsername, blackUsername);
    }

    @Override
    public String toString() {
        return "gameID=" + gameID +
                ", gameName='" + gameName + '\'' +
                ", whiteUsername='" + whiteUsername + '\'' +
                ", blackUsername='" + blackUsername + '\'';
    }
}
