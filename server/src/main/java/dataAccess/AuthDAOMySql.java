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
        return null;
    }

    @Override
    public void clear() {

    }

    @Override
    public void removeAuthToken(String authToken) {

    }
}
