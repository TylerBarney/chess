package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataAccess.UserDAO;

public class UserService {

    UserDAO userDAO;
    AuthDAO authDAO;
    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {
        //create user
        //get authToken
        if (userDAO.getUser(user.getUserName()) != null){
            throw new DataAccessException("403");
        }
        if (!user.isComplete()){
            throw new DataAccessException("400");
        }
        userDAO.createUser(user);
        AuthData authData = new AuthData(user.getUserName());
        authDAO.addAuthToken(authData);
        return authData;
    }

    public AuthData login(LoginRequest loginRequest) throws DataAccessException {
        UserData user = userDAO.getUser(loginRequest.username());
        if (user != null && user.getPassword().equals(loginRequest.password())){
            AuthData authData = new AuthData(loginRequest.username());
            authDAO.addAuthToken(authData);
            return authData;
        } else {
            throw new DataAccessException("401");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        //check if unauthorized
        if (authDAO.checkAuthToken(authToken) == null){
            throw new DataAccessException("401");
        }
        authDAO.removeAuthToken(authToken);
    }
}
