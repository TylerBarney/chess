package service;

import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.DAOInterfaces.GameDAO;
import model.AuthData;
import model.GameData;


import java.util.HashMap;

public class GameService  {
    public GameDAO gameDAO;
    public AuthDAO authDAO;

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
        String username = null;
        AuthData authData = authDAO.checkAuthToken(authToken);
        isValidJoin(playerColor, gameID, authToken);
        username = authData.getUserName();

        if (playerColor == null) {

        } else if (playerColor.toUpperCase().equals("WHITE")){
            gameData.setWhiteUsername(username);
        } else if (playerColor.toUpperCase().equals("BLACK")){
            gameData.setBlackUsername(username);
        }else {
            throw new DataAccessException("400");
        }
        gameDAO.update(gameData);
    }

    public void isValidJoin(String playerColor, int gameID, String authToken) throws DataAccessException{
        GameData gameData = gameDAO.getGame(gameID);
        if (gameData == null) throw new DataAccessException("400");
        if (authDAO.checkAuthToken(authToken) == null){
            throw new DataAccessException("401");
        }
        if (gameID <= 0) throw new DataAccessException("400");
        if (playerColor == null){

        } else if (playerColor.toUpperCase().equals("WHITE")){
            if (gameData.getWhiteUsername() != null && !authDAO.checkAuthToken(authToken).getUserName().equals(gameData.getWhiteUsername())) throw new DataAccessException("403");
        } else if (playerColor.toUpperCase().equals("BLACK")){
            String username = authDAO.checkAuthToken(authToken).getUserName();
            String blackUser = gameData.getBlackUsername();
            if (gameData.getBlackUsername() != null) {
                if (!authDAO.checkAuthToken(authToken).getUserName().equals(gameData.getBlackUsername())){
                    throw new DataAccessException("403");
                }
            }
        } else throw new DataAccessException("400");
    }
    public void clear(){
        gameDAO.clear();
        authDAO.clear();
    }
}
