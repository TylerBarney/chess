package dataAccessTests;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.sqlDAOs.GameDAOMySql;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.HashMap;

class GameDAOMySqlTest {

    GameDAOMySqlTest() throws SQLException, DataAccessException {
    }

    int tableSize(){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT COUNT(0) FROM games")){
                var response = preparedStatment.executeQuery();
                response.next();
                return response.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    boolean isInTable(GameData gameData){
        try (var conn = DatabaseManager.getConnection()){
            String preparedStatement = "SELECT * FROM games WHERE gameName = ? AND whiteUsername ";
            int gamePrepIndex = 2;
            if (gameData.getWhiteUsername() == null){
                preparedStatement += "IS NULL AND blackUsername ";
            } else {
                preparedStatement += "= ? AND blackUsername ";
                gamePrepIndex++;
            }
            if (gameData.getBlackUsername() == null){
                preparedStatement += "IS NULL";
            } else {
                preparedStatement += "= ?";
                gamePrepIndex++;
            }
            preparedStatement += " AND game = ?";
            try(var prep = conn.prepareStatement(preparedStatement)){
                prep.setString(1, gameData.getGameName());
                if (gameData.getWhiteUsername() != null) prep.setString(2, gameData.getWhiteUsername());
                if (gameData.getBlackUsername() != null) prep.setString(3, gameData.getBlackUsername());
                prep.setString(gamePrepIndex, new Gson().toJson(gameData.getGame()));

                try(var rs = prep.executeQuery()){
                    String gameName = null;
                    String whiteUsername = null;
                    String blackUsername = null;
                    ChessGame game = new ChessGame();
                    while (rs.next()){
                        gameName = rs.getString("gameName");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");
                        game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    }
                    GameData responseData = new GameData(gameName, whiteUsername, blackUsername, game);
                    if (responseData.equals(gameData)) return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }
    GameDAOMySql gameDAOMySql = new GameDAOMySql();

    @BeforeEach
    void setUp(){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("TRUNCATE TABLE games")){
                preparedStatment.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void createGame() throws DataAccessException {
        gameDAOMySql.createGame("testGame");
        GameData gameData = new GameData("testGame", null, null, new ChessGame(true));
        Assertions.assertTrue(isInTable(gameData));
    }

    @Test
    void noNameCreate(){
        Assertions.assertThrows(Throwable.class, () -> gameDAOMySql.createGame(null), "Didn't throw error");
    }

    @Test
    void getGame() throws DataAccessException {
        int gameID = gameDAOMySql.createGame("testGame");
        GameData gameData = new GameData(gameID,"testGame", null, null, new ChessGame(true));
        GameData responseData = gameDAOMySql.getGame(gameID);
        Assertions.assertEquals(gameData, responseData, "Did not get right game");
    }
    @Test
    void badGetGame() throws DataAccessException {
        int gameID = gameDAOMySql.createGame("testGame");
        GameData responseData = gameDAOMySql.getGame(-1);
        Assertions.assertNull(responseData, "Did not return null");
    }

    @Test
    void listGames() throws DataAccessException {
        HashMap<Integer, GameData> gameList = new HashMap<>();
        int gameID1 = gameDAOMySql.createGame("game1");
        int gameID2 = gameDAOMySql.createGame("game2");
        int gameID3 = gameDAOMySql.createGame("game3");
        gameList.put(gameID1, new GameData(gameID1, "game1", null, null, new ChessGame(true)));
        gameList.put(gameID2, new GameData(gameID2, "game2", null, null, new ChessGame(true)));
        gameList.put(gameID3, new GameData(gameID3, "game3", null, null, new ChessGame(true)));
        var responseList = gameDAOMySql.listGames();
        Assertions.assertEquals(gameList, responseList, "Lists are not equal");
    }

    @Test
    void noGamesListGames() throws DataAccessException {
        HashMap<Integer, GameData> gameList = new HashMap<>();
        var responseList = gameDAOMySql.listGames();
        Assertions.assertEquals(gameList, responseList, "Did not match empty list");
    }

    @Test
    void clear() throws DataAccessException {
        int gameID1 = gameDAOMySql.createGame("game1");
        int gameID2 = gameDAOMySql.createGame("game2");
        int gameID3 = gameDAOMySql.createGame("game3");
        gameDAOMySql.clear();
        Assertions.assertEquals(0, tableSize(), "Table is not clear");
    }
}