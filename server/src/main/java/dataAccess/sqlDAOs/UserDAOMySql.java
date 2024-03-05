package dataAccess.sqlDAOs;

import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.DataAccessException;
import model.UserData;

import java.sql.SQLException;

public class UserDAOMySql extends MySqlDataAccess implements UserDAO {
    public UserDAOMySql() throws SQLException, DataAccessException {
        configureDatabase();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()){
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES (?, ?, ?)")){
                preparedStatement.setString(1, user.getUserName());
                preparedStatement.setString(2, user.getPassword());
                preparedStatement.setString(3, user.getEmail());
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException("500");
        }


    }

    @Override
    public UserData getUser(String userName) {
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ?")){
                preparedStatment.setString(1, userName);
                try(var rs = preparedStatment.executeQuery()){
                    String username = null;
                    String password = null;
                    String email = null;
                    while (rs.next()){
                        username = rs.getString("username");
                        password = rs.getString("password");
                        email = rs.getString("email");
                    }
                    if (username == null || password == null || email == null) return null;
                    return new UserData(username, password, email);
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
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")){
                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {

        }
    }
}
