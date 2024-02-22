package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import dataAccess.UserDAOInterface;

public class UserService {

    UserDAOInterface userDAO;
    AuthDAO authDAO;
    public UserService(UserDAOInterface userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData user) throws DataAccessException {
        //create user
        //get authToken
        if (userDAO.getUser(user.getUserName()) != null){
            throw new DataAccessException("403");
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
}
