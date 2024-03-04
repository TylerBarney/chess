package DAOTests;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.GameDAOMySql;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOMySqlTest {

    GameDAOMySqlTest() throws SQLException, DataAccessException {
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
    @Test
    void createGame() throws DataAccessException {
        gameDAOMySql.createGame("testGame");
        GameData gameData = new GameData("testGame", null, null, new ChessGame(true));
        Assertions.assertTrue(isInTable(gameData));
    }

    @Test
    void getGame() {
    }

    @Test
    void listGames() {
    }

    @Test
    void clear() {
    }
}