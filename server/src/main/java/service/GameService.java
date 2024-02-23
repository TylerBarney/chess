package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.AuthData;
import model.GameData;

import javax.xml.crypto.Data;
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

    public void joinGame(String playerColor, int gameID, String authToken) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        String username;
        AuthData authData = authDAO.checkAuthToken(authToken);
        if (authData== null){
            throw new DataAccessException("401");
        } else {
            username = authData.getUserName();
        }
        if (gameID <= 0) throw new DataAccessException("400");

        if (playerColor == null) {

        } else if (playerColor.equals("WHITE")){
            if (gameData.getWhiteUsername() != null) throw new DataAccessException("403");
            gameData.setWhiteUsername(username);
        } else if (playerColor.equals("BLACK")){
            if (gameData.getBlackUsername() != null) throw new DataAccessException("403");
            gameData.setBlackUsername(username);
        }else {
            throw new DataAccessException("400");
        }

    }
}
