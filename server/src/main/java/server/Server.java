package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.DAOInterfaces.AuthDAO;
import dataAccess.DAOInterfaces.GameDAO;
import dataAccess.DAOInterfaces.UserDAO;
import dataAccess.memoryDAO.AuthDAOMemory;
import dataAccess.memoryDAO.GameDAOMemory;
import dataAccess.memoryDAO.UserDAOMemory;
import dataAccess.AuthDAOMySql;
import dataAccess.GameDAOMySql;
import dataAccess.UserDAOMySql;
import model.GameData;
import model.UserData;
import service.*;
import spark.*;

import java.util.HashMap;

public class Server {
    private UserDAO userDaoMemory = new UserDAOMemory();
    private AuthDAO authDAOMemory = new AuthDAOMemory();
    private GameDAO gameDAOMemory = new GameDAOMemory();
    private UserService userService;
    private GameService gameService;

    public Server() {
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        try{
            userService = new UserService(new UserDAOMySql(), new AuthDAOMySql());
            gameService = new GameService(new GameDAOMySql(), new AuthDAOMySql());
        } catch (Throwable ex){
            return -1;
        }

        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", (req, res) -> {userService.clear(); gameService.clear(); res.status(200); return "{}";});
        Spark.delete("/session", this::logoutHandler);
        Spark.post("/session", this::loginHandler);
        Spark.post("/game", this::createGameHandler);
        Spark.get("/game", this::listGameHandler);
        Spark.put("/game", this::joinGameHandler);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerHandler(Request req, Response res) {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            if (!user.isComplete()){
                throw new DataAccessException("400");
            }
            var authToken = userService.register(user);
            res.status(200);
            return new Gson().toJson(authToken);

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }

    private Object loginHandler(Request req, Response res) {
        try {
            var user = new Gson().fromJson(req.body(), LoginRequest.class);
            var authToken = userService.login(user);
            res.status(200);
            return new Gson().toJson(authToken);

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }
    private Object logoutHandler(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            userService.logout(authToken);
            res.status(200);
            return "{}";

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }

    private Object createGameHandler(Request req, Response res) {
        try {
            var gameName = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String authToken = req.headers("Authorization");
            res.status(200);
            return String.format("{ \"gameID\": %d }", gameService.createGame(gameName.gameName(), authToken));

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }
    private Object listGameHandler(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization");
            res.status(200);
            HashMap<Integer, GameData> gameList = gameService.listGames(authToken);
            ListResponse listResponse = new ListResponse(gameList.values());
            return new Gson().toJson(listResponse);

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }

    private Object joinGameHandler(Request req, Response res) {
        try {
            JoinRequest joinData = new Gson().fromJson(req.body(), JoinRequest.class);
            String authToken = req.headers("Authorization");
            String playerColor = joinData.playerColor();
            int gameID = joinData.gameID();
            res.status(200);
            gameService.joinGame(playerColor, gameID, authToken);
            return "{}";

        } catch(Throwable ex){
            return  handleError(ex, res);
        }
    }

    private Object handleError(Throwable ex, Response res){
        if (ex.getMessage().equals("401")){
            res.status(401);
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            return new Gson().toJson(errorMessage);
        }  else if (ex.getMessage().equals("403")){
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
