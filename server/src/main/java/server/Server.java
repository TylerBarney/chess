package server;

import com.google.gson.Gson;
import dataAccess.AuthDAOMemory;
import dataAccess.DataAccessException;
import model.UserData;
import dataAccess.UserDAOMemory;
import org.eclipse.jetty.server.Authentication;
import passoffTests.testClasses.TestException;
import service.ErrorMessage;
import service.UserService;
import spark.*;

public class Server {
    private UserDAOMemory userDaoMemory = new UserDAOMemory();
    private AuthDAOMemory authDAOMemory = new AuthDAOMemory();
    private UserService userService = new UserService(userDaoMemory, authDAOMemory);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/hello", (req, res) -> "Hello BYU!"); //sp server can start up
        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", (req, res) -> {userDaoMemory.clear(); authDAOMemory.clear(); res.status(200); return "";});
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerHandler(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var authToken = userService.register(user);
            res.status(200); //why isn't this responding with 200 code
            return new Gson().toJson(authToken);

        } catch(Throwable ex){
            if (ex.getMessage().equals("403")){
                res.status(403);
                ErrorMessage errorMessage = new ErrorMessage("Error: already taken");
                return new Gson().toJson(errorMessage);
            } else if (ex.getMessage().equals("400")){
                res.status(400);
                ErrorMessage errorMessage = new ErrorMessage("Error: bad request");
                return new Gson().toJson(errorMessage);
            }
            return null;
        }
    }

}