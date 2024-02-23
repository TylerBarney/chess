package server;

import com.google.gson.Gson;
import dataAccess.AuthDAOMemory;
import dataAccess.DataAccessException;
import dataAccess.GameDAOMemory;
import model.GameData;
import model.UserData;
import dataAccess.UserDAOMemory;
import org.eclipse.jetty.server.Authentication;
import passoffTests.testClasses.TestException;
import service.*;
import spark.*;

import java.util.Collection;
import java.util.HashMap;

public class Server {
    private UserDAOMemory userDaoMemory = new UserDAOMemory();
    private AuthDAOMemory authDAOMemory = new AuthDAOMemory();
    private GameDAOMemory gameDAOMemory = new GameDAOMemory();
    private UserService userService = new UserService(userDaoMemory, authDAOMemory);
    private GameService gameService = new GameService(gameDAOMemory, authDAOMemory);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.get("/hello", (req, res) -> "Hello BYU!"); //sp server can start up
        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", (req, res) -> {userDaoMemory.clear(); authDAOMemory.clear(); res.status(200); return "{}";});
        Spark.delete("/session", this::logoutHandler);
        Spark.post("/session", this::loginHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.get("/game", this::listGameHandler);
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
            if (!user.isComplete()){
                throw new DataAccessException("400");
            }
            var authToken = userService.register(user);
            res.status(200); //why isn't this responding with 200 code
            return new Gson().toJson(authToken);

        } catch(Throwable ex){
            if (ex.getMessage().equals("403")){
                res.status(403);
                ErrorMessage errorMessage = new ErrorMessage("Error: already taken");
                return new Gson().toJson(errorMessage);
            } else {
                res.status(400);
                ErrorMessage errorMessage = new ErrorMessage("Error: bad request");
                return new Gson().toJson(errorMessage);
            }

        }
    }

    private Object loginHandler(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), LoginRequest.class);
            var authToken = userService.login(user);
            res.status(200); //why isn't this responding with 200 code
            return new Gson().toJson(authToken);

        } catch(Throwable ex){
            if (ex.getMessage().equals("401")){
                res.status(401);
                ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
                return new Gson().toJson(errorMessage);
            }
            return null;
        }
    }
    private Object logoutHandler(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("Authorization");
            userService.logout(authToken);
            res.status(200); //why isn't this responding with 200 code
            return "{}";

        } catch(Throwable ex){
            if (ex.getMessage().equals("401")){
                res.status(401);
                ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
                return new Gson().toJson(errorMessage);
            }
            return null;
        }
    }

    private Object createGameHandler(Request req, Response res) throws DataAccessException {
        try {
            var gameName = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("Authorization");
            res.status(200);
            return String.format("{ \"gameID\": %d }", gameService.createGame(gameName.gameName(), authToken));

        } catch(Throwable ex){
            if (ex.getMessage().equals("401")){
                res.status(401);
                ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
                return new Gson().toJson(errorMessage);
            }
            return null;
        }
    }
    private Object listGameHandler(Request req, Response res) throws DataAccessException {
        try {
            String authToken = req.headers("Authorization");
            res.status(200);
            HashMap<Integer, GameData> gameList = gameService.listGames(authToken);
            String returned = new Gson().toJson(gameList.values());
            //System.out.println(returned);
            return returned;

        } catch(Throwable ex){
            if (ex.getMessage().equals("401")){
                res.status(401);
                ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
                return new Gson().toJson(errorMessage);
            }
            return null;
        }
    }



}
