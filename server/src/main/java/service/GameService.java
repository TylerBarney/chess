package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;

import java.util.HashMap;

public class GameService  {
    GameDAO gameDAO;
    AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public int createGame(String gameName, String authToken) throws DataAccessException {
        if (authDAO.checkAuthToken(authToken) == null){
            throw new DataAccessException("401");
        }
        return gameDAO.createGame(gameName);
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws DataAccessException {
        if (authDAO.checkAuthToken(authToken) == null){
            throw new DataAccessException("401");
        }
        return gameDAO.listGames();
    }
}
