package dataAccess;

import model.AuthData;
import model.UserData;

public interface AuthDAO {
    public void addAuthToken(AuthData authToken);
    public AuthData checkAuthToken(String authToken);

    public void clear();
    public void removeAuthToken(String authToken);
}
