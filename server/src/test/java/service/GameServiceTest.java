package service;

import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {

    private UserDAOInterface userDAO = new UserDAOMemory();
    private AuthDAO authDAO = new AuthDAOMemory();
    private GameDAO gameDAO = new GameDAOMemory();
    private UserService userService = new UserService(userDAO, authDAO);
    private GameService gameService = new GameService(gameDAO, authDAO);
    @BeforeEach
    public void setUp(){
        userDAO = new UserDAOMemory();
        authDAO = new AuthDAOMemory();
        userService = new UserService(userDAO, authDAO);
        gameDAO = new GameDAOMemory();
        gameService = new GameService(gameDAO, authDAO);
    }
    @Test
    void createGame() throws DataAccessException {
        String gameName = "Hello";
        GameData exampleGame = new GameData(gameName);
        int returnID = gameService.createGame(gameName, "kjhlkjh");
        Assertions.assertNotEquals(returnID, -1, "Game not created");
        Assertions.assertEquals(gameDAO.getGame(returnID).getGameName(), gameName, "Game names do not match");
        Assertions.assertEquals(exampleGame.getGame(),
                gameDAO.getGame(returnID).getGame(),
                "Game boards do not match");

    }
    @Test
    void noNameCreateGame(){
        Assertions.assertThrows(DataAccessException.class, () -> gameService.createGame(null, "kjhl"),
                "Doesn't throw error when no Game name");
    }

    @Test
    void badAuthCreate(){
        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.createGame("Game Name", null));
    }

    @Test
    void successGamesListed() throws DataAccessException {
        HashMap<Integer, GameData> exampleList = new HashMap<>();
        exampleList.put(1, new GameData(1, "Game1"));
        exampleList.put(2, new GameData(2, "Game2"));
        exampleList.put(3, new GameData(3, "Game3"));

        UserData exampleUser = new UserData("Test1", "test2", "test@test");
        AuthData returnData = userService.register(exampleUser);

        gameService.createGame("Game1", returnData.getAuthToken());
        gameService.createGame("Game2", returnData.getAuthToken());
        gameService.createGame("Game3", returnData.getAuthToken());

        HashMap<Integer, GameData> returnedList = gameService.listGames(returnData.getAuthToken());
        Assertions.assertEquals(exampleList, returnedList, "Lists are not equal");
    }
    @Test
    void unAuthedGameList(){
        Assertions.assertThrows(DataAccessException.class,
                () -> gameService.listGames(null), "Didn't throw bad auth");

    }
}