package server;

import com.google.gson.Gson;
import dataAccess.*;
import dataAccess.AuthDAOMySql;
import dataAccess.GameDAOMySql;
import dataAccess.UserDAOMySql;
import model.*;
import model.webSocketMessages.ErrorMessage;
import service.*;
import service.CreateGameRequest;
import service.LoginRequest;
import spark.*;

import java.util.HashMap;

public class Server {
    private UserService userService;
    private GameService gameService;
    private WebSocketHandler webSocketHandler;

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
        webSocketHandler = new WebSocketHandler(userService, gameService);
        Spark.webSocket("/connect", webSocketHandler);
        Spark.post("/user", this::registerHandler);
        Spark.delete("/db", this::clearHandler);
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

    public Object clearHandler(Request req, Response res){
        clear();
        res.status(200);
        return "{}";
    }
    public void clear() {
        userService.clear();
        gameService.clear();
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
            ErrorException errorException = new ErrorException("Error: unauthorized");
            return new Gson().toJson(errorException);
        }  else if (ex.getMessage().equals("403")){
            res.status(403);
            ErrorException errorException = new ErrorException("Error: already taken");
            return new Gson().toJson(errorException);
        } else {
            res.status(400);
            ErrorException errorException = new ErrorException("Error: bad request");
            return new Gson().toJson(errorException);
        }
    }



}
