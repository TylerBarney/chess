package dataAccess;

import model.AuthData;

import java.sql.SQLException;

public class AuthDAOMySql extends MySqlDataAccess implements AuthDAO{
    public AuthDAOMySql() throws SQLException, DataAccessException {
        configureDatabase();
    }

    @Override
    public void addAuthToken(AuthData authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auths (authToken, username) VALUES (?, ?)")){
                preparedStatement.setString(1, authToken.getAuthToken());
                preparedStatement.setString(2, authToken.getUserName());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("500");
        }
    }

    @Override
    public AuthData checkAuthToken(String authToken) {
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken = ?")){
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()){
                    String username = "";
                    while(rs.next()){
                        username = rs.getString("username");
                    }
                    return new AuthData(username, authToken);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")){
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAuthToken(String authToken) {
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken = ?")){
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
