package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import model.webSocketMessages.JoinPlayerCommand;
import model.webSocketMessages.NotificationMessage;
import model.webSocketMessages.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import javax.management.Notification;
import java.io.IOException;

@WebSocket
public class WebSocketHandler {
    private ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println("Server recieved Message");
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(UserGameCommand.class, (JsonDeserializer<UserGameCommand>) (el, type, ctx) -> {
            UserGameCommand command = null;
            if (el.isJsonObject()){
                String commandType = el.getAsJsonObject().get("commandType").getAsString();
                switch (UserGameCommand.CommandType.valueOf(commandType)){
                    case JOIN_PLAYER -> command = ctx.deserialize(el, JoinPlayerCommand.class);
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
        }
    }

    private void joinCommand(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        System.out.println("Running command on server");
        connections.add(gameID, authToken, playerColor, session);
        var message = String.format("player joined as %s", playerColor.name());
        var notfication = new NotificationMessage(message);
        connections.broadcast(gameID, authToken, notfication);
        System.out.println(message);
    }
}
