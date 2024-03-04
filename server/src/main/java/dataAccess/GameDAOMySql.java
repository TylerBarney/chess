package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class GameDAOMySql extends MySqlDataAccess implements GameDAO{
    public GameDAOMySql() throws SQLException, DataAccessException {
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("INSERT INTO games (gameName, game) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)){
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, new Gson().toJson(new ChessGame(true)));
                preparedStatement.executeUpdate();
                var rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }

    @Override
    public GameData getGame(int gameID) {
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM games WHERE gameID = ?")){
                preparedStatement.setInt(1, gameID);
                try(var rs = preparedStatement.executeQuery()){
                    String gameName = null;
                    String whiteUsername = null;
                    String blackUsername = null;
                    ChessGame game = null;
                    while (rs.next()){
                        gameName = rs.getString("gameName");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");
                        game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    }
                    return new GameData(gameID, gameName, whiteUsername, blackUsername, game);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HashMap<Integer, GameData> listGames() {
        HashMap<Integer, GameData> gameList = new HashMap<>();
        try(var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("SELECT * FROM games")){
                try(var rs = preparedStatement.executeQuery()){
                    String gameName;
                    String whiteUsername;
                    String blackUsername;
                    ChessGame game;
                    int gameID;
                    while (rs.next()){
                        gameID = rs.getInt("gameID");
                        gameName = rs.getString("gameName");
                        whiteUsername = rs.getString("whiteUsername");
                        blackUsername = rs.getString("blackUsername");
                        game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        gameList.put(gameID, new GameData(gameID, gameName, whiteUsername, blackUsername, game));
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
        return gameList;
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")){
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
