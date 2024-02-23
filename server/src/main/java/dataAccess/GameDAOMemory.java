package dataAccess;

import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class GameDAOMemory implements GameDAO{
    HashMap<Integer, GameData> gameList;

    public GameDAOMemory(){
        gameList = new HashMap<>();
    }
    @Override
    public int createGame(String gameName) {
        int gameID = gameList.size() + 1;
        gameList.put(gameID, new GameData(gameID, gameName));
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameList.get(gameID);
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        return gameList;
    }

    @Override
    public void updateGame() {

    }
}
