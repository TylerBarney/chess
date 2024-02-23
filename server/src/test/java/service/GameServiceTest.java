package service;

import dataAccess.*;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}