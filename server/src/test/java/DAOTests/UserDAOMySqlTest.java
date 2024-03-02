package DAOTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAOMySql;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOMySqlTest {

    UserDAOMySql userDAOMySql = new UserDAOMySql();

    UserDAOMySqlTest() throws SQLException, DataAccessException {
    }


    boolean isInTable(UserData userData){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ? AND password = ? AND email = ?")){
                preparedStatment.setString(1, userData.getUserName());
                preparedStatment.setString(2, userData.getPassword());
                preparedStatment.setString(3, userData.getEmail());
                try(var rs = preparedStatment.executeQuery()){
                    String username = "";
                    String password = "";
                    String email = "";
                    while (rs.next()){
                        username = rs.getString("username");
                        password = rs.getString("password");
                        email = rs.getString("email");
                    }
                    UserData responseData = new UserData(username, password, email);
                    if (responseData.equals(userData)) return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    int tableSize(){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT COUNT(0) FROM users")){
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

    @BeforeEach
    void setUp(){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("TRUNCATE TABLE users")){
                preparedStatment.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    void createUser() throws SQLException, DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        Assertions.assertTrue(isInTable(userData), "user is not in table");
    }

    @Test
    void getUser() throws SQLException, DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        UserData responseData = userDAOMySql.getUser(userData.getUserName());
        Assertions.assertEquals(userData, responseData, "User datas do not match up");
    }
    @Test
    void clearUsers() throws DataAccessException {
        userDAOMySql.createUser(new UserData("username1", "password1", "email1"));
        userDAOMySql.createUser(new UserData("username2", "password2", "email2"));
        userDAOMySql.createUser(new UserData("username3", "password3", "email3"));
        userDAOMySql.clear();
        Assertions.assertEquals(0, tableSize(), "Table is not clear");
    }
}