package ui;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.ErrorException;
import model.webSocketMessages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DeploymentException, URISyntaxException, IOException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;


            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    var serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    NotificationMessage notification;
                    LoadGameMessage loadGameMessage;
                    ErrorMessage errorMessage;

                    if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION){
                        notification = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                        loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(loadGameMessage);
                    } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR){
                        errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(errorMessage);
                    }
                }
            });
        } catch (Throwable ex){
            String getClass = ex.getClass().getName();
            if (ex.getClass().getName().equals("NumberFormatException")){

            }
            throw ex;
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws IOException {
        try {
            if (playerColor == null){
                joinObserver(authToken, gameID);
            } else{
                var command = new JoinPlayerCommand(authToken, gameID, playerColor);
                this.session.getBasicRemote().sendText(new Gson().toJson(command));
            }
        } catch (Exception ex){
            throw ex;
        }
    }
    public void joinObserver(String authToken, int gameID) throws IOException {
        try {
            var command = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            throw ex;
        }
    }
    public void resign(String authToken, int gameID) throws IOException{
        try {
            var command = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            throw ex;
        }
    }

    public void leave(String authToken, int gameID) throws IOException {
        try {
            var command = new LeaveCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            throw ex;
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        try {
            var command = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            throw ex;
        }
    }

    public void setNotificationHandler(NotificationHandler notificationHandler){
        this.notificationHandler = notificationHandler;
    }
}
