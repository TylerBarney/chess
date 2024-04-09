package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import dataAccess.DataAccessException;
import model.GameData;
import model.webSocketMessages.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;

import javax.management.Notification;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();
    private UserService userService;
    private GameService gameService;
    public WebSocketHandler(UserService userService, GameService gameService){
        this.userService = userService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserGameCommand.class, (JsonDeserializer<UserGameCommand>) (el, type, ctx) -> {
            UserGameCommand command = null;
            if (el.isJsonObject()){
                String commandType = el.getAsJsonObject().get("commandType").getAsString();
                switch (UserGameCommand.CommandType.valueOf(commandType)){
                    case JOIN_PLAYER -> command = ctx.deserialize(el, JoinPlayerCommand.class);
                    case JOIN_OBSERVER -> command = ctx.deserialize(el, JoinObserverCommand.class);
                    case MAKE_MOVE -> command = ctx.deserialize(el, MakeMoveCommand.class);
                    case RESIGN -> command = ctx.deserialize(el, ResignCommand.class);
                    case LEAVE -> command = ctx.deserialize(el, LeaveCommand.class);
                }
            }
            return command;
        });
        Gson gson = gsonBuilder.create();
        var command = gson.fromJson(message, UserGameCommand.class);
        switch(command.getCommandType()){
            case JOIN_PLAYER -> {
                var joinCommand = (JoinPlayerCommand) command;
                joinCommand(joinCommand.getAuthString(), joinCommand.gameID, joinCommand.playerColor, session);
            }
            case JOIN_OBSERVER -> {
                var joinCommand = (JoinObserverCommand) command;
                joinCommand(joinCommand.getAuthString(), joinCommand.gameID, null, session);
            }
            case MAKE_MOVE -> {
                var moveCommand = (MakeMoveCommand) command;
                moveCommand(moveCommand.getAuthString(), moveCommand.gameID, moveCommand.move, session);
            }
            case RESIGN -> {
                var resignCommand = (ResignCommand) command;
                resignCommand(resignCommand.getAuthString(), resignCommand.gameID, session);
            }
            case LEAVE -> {
                var leaveCommand = (LeaveCommand) command;
                leaveCommand(leaveCommand.getAuthString(), leaveCommand.gameID, session);
            }
        }
    }

    private void joinCommand(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        try{
            String teamColor = (playerColor!= null) ? String.valueOf(playerColor) : null;
            gameService.isValidJoin(teamColor, gameID, authToken);
            connections.add(gameID, authToken, playerColor, session);
            String userName = userService.authDAO.checkAuthToken(authToken).getUserName();
            var message = (playerColor != null) ? String.format("%s joined as %s", userName, playerColor.name()) : String.format("%s joined as observer", userName);
            var notfication = new NotificationMessage(message);
            connections.broadcast(gameID, authToken, notfication);
            ChessGame game = gameService.gameDAO.getGame(gameID).getGame();
            connections.broadCastTo(gameID, authToken, new LoadGameMessage(game));
        } catch (DataAccessException ex){
            sendErrorMessage(authToken, handleErrors(ex), session);
        }
    }

    private void moveCommand(String authToken, int gameID, ChessMove move, Session session) throws IOException {
        try {
            ChessGame game = gameService.gameDAO.getGame(gameID).getGame();
            ChessGame.TeamColor turn = game.getTeamTurn();
            ChessGame.TeamColor playerColor = connections.getColorByAuth(authToken, gameID);
            if (game.getTeamTurn() != connections.getColorByAuth(authToken, gameID)) throw new InvalidMoveException("Invalid Move");
            game.makeMove(move);
            GameData gameData = gameService.gameDAO.getGame(gameID).setGame(game);
            gameService.gameDAO.update(gameData);
            gameData = gameService.gameDAO.getGame(gameID);
            String userName = userService.authDAO.checkAuthToken(authToken).getUserName();
            connections.broadcast(gameID, "0", new LoadGameMessage(game));
            connections.broadcast(gameID, authToken, new NotificationMessage(String.format("%s made %s move", userName, move.toString())));
        } catch (InvalidMoveException ex){
            sendErrorMessage(authToken, handleErrors(ex), session);
        }
    }

    private void resignCommand(String authToken, int gameID, Session session) throws IOException {
        try{
            GameData gameData = gameService.gameDAO.getGame(gameID);
            String userName = userService.authDAO.checkAuthToken(authToken).getUserName();
            ChessGame game = gameData.getGame();
            if (game.isGameOver) throw new InvalidMoveException("Game is already over");
            if (!gameData.getBlackUsername().equals(userName) && !gameData.getWhiteUsername().equals(userName)) throw new InvalidMoveException("Can't resign as observer");
            game.isGameOver = true;
            gameData = gameService.gameDAO.getGame(gameID).setGame(game);
            gameService.gameDAO.update(gameData);
            connections.broadcast(gameID, "0", new NotificationMessage(String.format("%s has resigned", userName)));
        } catch (InvalidMoveException ex){
            sendErrorMessage(authToken, handleErrors(ex), session);
        }


        session.close();
    }

    private void leaveCommand(String authToken, int gameID, Session session) throws IOException {
        GameData gameData = gameService.gameDAO.getGame(gameID);
        String userName = userService.authDAO.checkAuthToken(authToken).getUserName();
        if (userName.equals(gameData.getWhiteUsername())){
            gameData.setWhiteUsername(null);
        } else if (userName.equals(gameData.getBlackUsername())){
            gameData.setBlackUsername(null);
        }
        gameService.gameDAO.update(gameData);
        session.close();
        connections.broadcast(gameID, authToken, new NotificationMessage(String.format("%s has left the game", userName)));

    }

    public void sendErrorMessage(String authToken, ErrorMessage errorMessage, Session session) throws IOException {
        connections.broadCastError(authToken, errorMessage, session);
    }

    public ErrorMessage handleErrors(DataAccessException ex){
        if (ex.getMessage().equals("401")){
            return new ErrorMessage("Error: unauthorized");
        } else if (ex.getMessage().equals("403")){
            return new ErrorMessage("Error: already taken");
        } else if (ex.getMessage().equals("400")){
            return new ErrorMessage("Error: bad request");
        } else {
            return new ErrorMessage("Error: " + ex.getMessage());
        }
    }
    public ErrorMessage handleErrors(InvalidMoveException ex){
        return new ErrorMessage("Error: " + ex.getMessage());
    }

}
