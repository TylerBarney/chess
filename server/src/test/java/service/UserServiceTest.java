package service;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserDAOInterface userDAO = new UserDAOMemory();
    private AuthDAO authDAO = new AuthDAOMemory();
    private UserService userService = new UserService(userDAO, authDAO);
    @BeforeEach
    public void setUp(){
        userDAO = new UserDAOMemory();
        authDAO = new AuthDAOMemory();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    void normalRegister() throws DataAccessException {
        UserData exampleUser = new UserData("Test1", "test2", "test@test");
        AuthData returnData = userService.register(exampleUser);
        Assertions.assertNotNull(returnData, "No Data Returned");
        Assertions.assertNotNull(returnData.getAuthToken(), "No authToken returned");
        Assertions.assertNotNull(returnData.getUserName(), "No username returned");
        Assertions.assertEquals(returnData.getUserName(), exampleUser.getUserName(), "Username does not match");
        Assertions.assertEquals(userDAO.getUser(exampleUser.getUserName()), exampleUser,"UserData not added to UserDAO");
        Assertions.assertNotNull(authDAO.checkAuthToken(returnData.getAuthToken()), "AuthData not added to AuthDAO");
    }
    @Test
    void badRegister() throws DataAccessException {
        UserData exampleUser = new UserData(null, "test2", "test@test");
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(exampleUser), "Does not throw error");
    }
    @Test
    void alreadyTakenRegister() throws DataAccessException {
        UserData exampleUser = new UserData("test1", "test2", "test@test");
        userService.register(exampleUser);
        Assertions.assertThrows(DataAccessException.class, () -> userService.register(exampleUser), "Does not throw error");
    }

    @Test
    void successfulLogin() throws DataAccessException {
        UserData exampleUser = new UserData("test1", "test2", "test@test");
        LoginRequest exampleLogin = new LoginRequest(exampleUser.getUserName(), exampleUser.getPassword());
        userService.register(exampleUser);
        AuthData returnData = userService.login(exampleLogin);
        Assertions.assertNotNull(returnData, "No Data Returned");
        Assertions.assertNotNull(returnData.getAuthToken(), "No authToken returned");
        Assertions.assertNotNull(returnData.getUserName(), "No username returned");
        Assertions.assertNotNull(authDAO.checkAuthToken(returnData.getAuthToken()), "AuthData not added to AuthDAO");
    }
    @Test
    void unAuthorizedLogin() throws DataAccessException {
        UserData exampleUser = new UserData("test1", "test2", "test@test");
        LoginRequest wrongUsernameLogin = new LoginRequest("Fred", exampleUser.getPassword());
        LoginRequest wrongPasswordLogin = new LoginRequest("Fred", exampleUser.getPassword());
        userService.register(exampleUser);
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(wrongUsernameLogin),
                "Does not throw error for incorrect username");
        Assertions.assertThrows(DataAccessException.class, () -> userService.login(wrongPasswordLogin),
                "Does not throw error for incorrect password");
    }

    @Test
    void successfulLogOut() throws DataAccessException {
        UserData exampleUser = new UserData("test1", "test2", "test@test");
        LoginRequest exampleLogin = new LoginRequest(exampleUser.getUserName(), exampleUser.getPassword());
        userService.register(exampleUser);
        AuthData authData = userService.login(exampleLogin);
        userService.logout(authData.getAuthToken());
        Assertions.assertNull(authDAO.checkAuthToken(authData.getAuthToken()), "AuthToken still in AuthDAO");
    }

    @Test
    void unAuthorizedLogout() throws DataAccessException {
        UserData exampleUser = new UserData("test1", "test2", "test@test");
        userService.register(exampleUser);
        Assertions.assertThrows(DataAccessException.class, () -> userService.logout("6"),
                "Does not throw error for unauthorized Logout");
    }

}