package dataAccess.DAOInterfaces;

import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public void addAuthToken(AuthData authToken) throws DataAccessException;
    public AuthData checkAuthToken(String authToken);

    public void clear();
    public void removeAuthToken(String authToken);
}
