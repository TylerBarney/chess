package service;

import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.memoryDAO.AuthDAOMemory;
import dataAccess.memoryDAO.UserDAOMemory;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class UserServiceTest {

    private UserDAO userDAO = new UserDAOMySql();
    private AuthDAO authDAO = new AuthDAOMySql();
    private UserService userService = new UserService(userDAO, authDAO);

    UserServiceTest() throws DataAccessException {
    }

    boolean areUsersEqual(UserData user1, UserData user2){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (user1.isComplete() && user2.isComplete()){
            if (user1.getUserName().equals(user2.getUserName()) && user1.getEmail().equals(user2.getEmail()) && (encoder.matches(user1.getPassword(), user2.getPassword()) || encoder.matches(user2.getPassword(), user1.getPassword()))){
                return true;
            }
        }
        return false;
    }
    @BeforeEach
    public void setUp(){
        userDAO.clear();
        authDAO.clear();
    }

    @Test
    void normalRegister() throws DataAccessException {
        UserData exampleUser = new UserData("Test1", "test2", "test@test");
        AuthData returnData = userService.register(exampleUser);
        Assertions.assertNotNull(returnData, "No Data Returned");
        Assertions.assertNotNull(returnData.getAuthToken(), "No authToken returned");
        Assertions.assertNotNull(returnData.getUserName(), "No username returned");
        Assertions.assertEquals(returnData.getUserName(), exampleUser.getUserName(), "Username does not match");
        Assertions.assertTrue(areUsersEqual(userDAO.getUser(exampleUser.getUserName()), exampleUser),"UserData not added to UserDAO");
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
        LoginRequest exampleLogin = new LoginRequest(exampleUser.getUserName(), "test2");
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
        LoginRequest exampleLogin = new LoginRequest(exampleUser.getUserName(), "test2");
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