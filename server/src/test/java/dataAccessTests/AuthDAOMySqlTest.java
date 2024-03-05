package dataAccessTests;

import dataAccess.sqlDAOs.AuthDAOMySql;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class AuthDAOMySqlTest {

    int tableSize(){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT COUNT(0) FROM auths")){
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

    AuthDAOMySql authDAOMySql = new AuthDAOMySql();

    AuthDAOMySqlTest() throws SQLException, DataAccessException {
    }
    boolean isInTable(AuthData authData){
        try (var conn = DatabaseManager.getConnection()){
            try(var preparedStatment = conn.prepareStatement("SELECT authToken, username FROM auths WHERE username = ? AND authToken = ?")){
                preparedStatment.setString(2, authData.getAuthToken());
                preparedStatment.setString(1, authData.getUserName());
                try(var rs = preparedStatment.executeQuery()){
                    String username = "";
                    String authToken = "";
                    while (rs.next()){
                        username = rs.getString("username");
                        authToken = rs.getString("authToken");
                    }
                    AuthData responseData = new AuthData(username, authToken);
                    if (responseData.equals(authData)) return true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Test
    void addAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        Assertions.assertTrue(isInTable(authData), "Not found in table");
    }

    @Test
    void badAddAuthToken() throws DataAccessException {
        AuthData authData = new AuthData(null);
        Assertions.assertThrows(Throwable.class,() -> authDAOMySql.addAuthToken(authData), "Did not throw error");
    }

    @Test
    void checkAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        var responseData = authDAOMySql.checkAuthToken(authData.getAuthToken());
        Assertions.assertEquals(authData, responseData, "Does not return correct data");
    }

    @Test
    void badCheckAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        var responseData = authDAOMySql.checkAuthToken("Not an authToken");
        Assertions.assertNull(responseData, "Does not return null");

    }

    @Test
    void clear() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        AuthData authData2 = new AuthData("Test2");
        authDAOMySql.addAuthToken(authData2);
        AuthData authData3 = new AuthData("Test3");
        authDAOMySql.addAuthToken(authData3);
        authDAOMySql.clear();
        Assertions.assertEquals(0, tableSize(), "Table isn't clear");
    }

    @Test
    void removeAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        AuthData authData2 = new AuthData("Test2");
        authDAOMySql.addAuthToken(authData2);
        AuthData authData3 = new AuthData("Test3");
        authDAOMySql.removeAuthToken(authData3.getAuthToken());
        Assertions.assertFalse(isInTable(authData3), "Still in table");
    }
    @Test
    void badRemoveAuthToken() throws DataAccessException {
        AuthData authData = new AuthData("Test1");
        authDAOMySql.addAuthToken(authData);
        AuthData authData2 = new AuthData("Test2");
        authDAOMySql.addAuthToken(authData2);
        AuthData authData3 = new AuthData("Test3");
        authDAOMySql.removeAuthToken("Not an AuthToken");
        Assertions.assertFalse(tableSize() == 3, "Deleted when it shouldn't have");
    }
}