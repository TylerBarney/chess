package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.UserData;

public interface UserDAO {

    void createUser(UserData user) throws DataAccessException;
    UserData getUser(String userName);
    void clear();
}
