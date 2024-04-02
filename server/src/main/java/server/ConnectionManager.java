package server;

import chess.ChessGame;
import model.webSocketMessages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<Integer, Connection> connections = new ConcurrentHashMap<>();

    public void add(int gameID, String authToken, ChessGame.TeamColor playerColor, Session session){
        var connection = new Connection(authToken, playerColor, session);
        connections.put(gameID, connection);
    }

    public void remove(String clientAuth){

        for (Map.Entry<Integer, Connection> entry : connections.entrySet()){
            String entryAuth = entry.getValue().authToken;

            if (entryAuth.equals(clientAuth)) {
                connections.remove(entry.getKey(), entry.getValue());
            }
        }
    }

    public void broadcast(int gameID, String excludeClient, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Map.Entry<Integer, Connection> entry : connections.entrySet()){
            if (entry.getKey() == gameID){
                if (entry.getValue().session.isOpen()){
                    if (!entry.getValue().authToken.equals(excludeClient)){
                        entry.getValue().send(notification.toString());
                    }
                } else {
                    removeList.add(entry.getValue());
                }
            }
        }
        for (var c : removeList){
            remove(c.authToken);
        }
    }
}
