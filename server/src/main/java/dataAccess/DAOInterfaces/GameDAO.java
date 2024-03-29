package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public interface GameDAO {
    public int createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameID);
    public HashMap<Integer, GameData> listGames();
    public void clear();

    public void update(GameData gameData);
}
