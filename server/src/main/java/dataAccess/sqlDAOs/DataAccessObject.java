package dataAccess.sqlDAOs;

import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.memoryDAO.AuthDAOMemory;
import dataAccess.memoryDAO.GameDAOMemory;
import dataAccess.memoryDAO.UserDAOMemory;

public class DataAccessObject {

    private GameDAO gameDAOmemory = new GameDAOMemory();
    private AuthDAO authDAOMemory = new AuthDAOMemory();
    private UserDAO userDAOMemory = new UserDAOMemory();
}
