package dataAccess.memoryDAO;

import dataAccess.DAOInterfaces.AuthDAO;
import model.AuthData;

import java.util.HashMap;

public class AuthDAOMemory implements AuthDAO {

    private HashMap<String, String> authMap = new HashMap<>();
    @Override
    public void addAuthToken(AuthData authToken) {

        authMap.put(authToken.getAuthToken(), authToken.getUserName());
    }

    @Override
    public AuthData checkAuthToken(String authToken) {
        if (authToken == null){
            return null;
        }
        String username = authMap.get(authToken);
        if (username != null){
            return new AuthData(username, authToken);
        }
        return null;
    }

    public void removeAuthToken(String authToken){
        authMap.remove(authToken);
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
