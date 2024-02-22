package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAOMemory implements AuthDAO{

    private HashMap<String, AuthData> authMap = new HashMap<>();
    @Override
    public void addAuthToken(AuthData authToken) {
        authMap.put(authToken.getAuthToken(), authToken);
    }

    @Override
    public void checkAuthToken(AuthData authToken) {
        authMap.get(authToken.getAuthToken());
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
