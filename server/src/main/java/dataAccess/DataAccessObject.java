package dataAccess;

public class DataAccessObject {

    private GameDAO gameDAOmemory = new GameDAOMemory();
    private AuthDAO authDAOMemory = new AuthDAOMemory();
    private UserDAO userDAOMemory = new UserDAOMemory();
}
