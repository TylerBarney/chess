package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import dataAccess.UserDAOMySql;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;

class UserDAOMySqlTest {

    UserDAOMySql userDAOMySql = new UserDAOMySql();

    UserDAOMySqlTest() throws SQLException, DataAccessException {
    }
    boolean areUsersEqual(UserData user1, UserData user2){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user1.isComplete() && user2.isComplete()){
            if (user1.getUserName().equals(user2.getUserName()) && user1.getEmail().equals( user2.getEmail())){
                if (encoder.matches(user1.getPassword(), user2.getPassword()) || encoder.matches(user2.getPassword(), user1.getPassword())) return true;
            }
        }
        return false;
    }
    boolean isInTable(UserData userData){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT username, password, email FROM users WHERE username = ? AND email = ?")){
                preparedStatment.setString(1, userData.getUserName());
                preparedStatment.setString(2, userData.getEmail());
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
                    if (areUsersEqual(responseData, userData)) return true;
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
    void createUser() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        Assertions.assertTrue(isInTable(userData), "user is not in table");
    }

    @Test
    void repeatedUser() throws DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        Assertions.assertThrows(Throwable.class, () -> userDAOMySql.createUser(userData), "Doesn't throw error");
    }

    @Test
    void getUser() throws SQLException, DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        UserData responseData = userDAOMySql.getUser(userData.getUserName());
        Assertions.assertTrue(areUsersEqual(userData, responseData), "User datas do not match up");
    }
    @Test
    void getBadUser() throws SQLException, DataAccessException {
        UserData userData = new UserData("username", "password", "email");
        userDAOMySql.createUser(userData);
        UserData responseData = userDAOMySql.getUser("Not a user");
        Assertions.assertNull(responseData, "Is not null");
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