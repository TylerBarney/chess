package dataAccess;

import model.UserData;

import java.util.HashMap;

public class UserDAOMemory implements UserDAO {
    private HashMap<String, UserData> userMap = new HashMap<>();

    @Override
    public void createUser(UserData user) {
        userMap.put(user.getUserName(), user);
    }

    @Override
    public UserData getUser(String userName) {
        return userMap.get(userName);
    }

    @Override
    public void clear() {
        userMap.clear();
    }
}
