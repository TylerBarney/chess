package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public void addAuthToken(AuthData authToken);
    public void checkAuthToken(AuthData authToken);
    public void clear();
}
