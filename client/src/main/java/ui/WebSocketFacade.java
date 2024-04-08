package ui;

import chess.ChessGame;
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
//                    GsonBuilder gsonBuilder = new GsonBuilder();
//                    gsonBuilder.registerTypeAdapter(ServerMessage.class, (JsonDeserializer<ServerMessage>) (el, type, ctx) -> {
//                        ServerMessage serverMessage = null;
//                        if (el.isJsonObject()){
//                            String serverMessageType = el.getAsJsonObject().get("serverMessageType").getAsString();
//                            switch (ServerMessage.ServerMessageType.valueOf(serverMessageType)){
//                                case NOTIFICATION -> serverMessage = ctx.deserialize(el, NotificationMessage.class);
//                                case LOAD_GAME -> serverMessage = ctx.deserialize(el, LoadGameMessage.class);
//                                case ERROR -> serverMessage = ctx.deserialize(el, ErrorMessage.class);
//                            }
//                        }
//                        return serverMessage;
//                    });
//                    Gson gson = gsonBuilder.create();
//                    var serverMessage = gson.fromJson(message, ServerMessage.class);
//                    switch(serverMessage.getServerMessageType()){
//                        case NOTIFICATION -> {
//                            var notificationMessage = (NotificationMessage) serverMessage;
//                            notificationHandler.notify(notificationMessage);
//                        }
//                        case LOAD_GAME -> {
//                            var loadGameMessage = (LoadGameMessage) serverMessage;
//                            notificationHandler.notify(loadGameMessage);
//                        }
//                        case ERROR -> {
//                            var errorMessage = (ErrorMessage) serverMessage;
//                            notificationHandler.notify(serverMessage);
//                        }
//                    }
                    //need to edit this to handle other messages like load game
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
        } catch (Exception ex){
            throw ex;
        }
    }
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws IOException {
        try {
            var command = new JoinPlayerCommand(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception ex){
            throw ex;
        }
    }
}
