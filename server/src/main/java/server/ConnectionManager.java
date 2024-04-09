package server;

import chess.ChessGame;
import model.webSocketMessages.ErrorMessage;
import model.webSocketMessages.ServerMessage;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public ConcurrentHashMap<Integer, Vector<Connection>> connections = new ConcurrentHashMap<>();

    public ChessGame.TeamColor getColorByAuth(String auth, int gameID){
        for (var connection : connections.get(gameID)){
            if (auth.equals(connection.authToken)){
                return connection.playerColor;
            }
        }
        return null;
    }
    public void add(int gameID, String authToken, ChessGame.TeamColor playerColor, Session session){
        var connection = new Connection(authToken, playerColor, session);
        if (connections.containsKey(gameID)){
            connections.get(gameID).add(connection);
        } else {
            Vector<Connection> newConnection = new Vector<>();
            newConnection.add(connection);
            connections.put(gameID, newConnection);
        }

    }

    public void remove(String clientAuth){

        for (Map.Entry<Integer, Vector<Connection>> entry : connections.entrySet()){
            Vector<Connection> entryConnections = entry.getValue();
            for (var connection : entryConnections){
                if (connection.authToken.equals(clientAuth)){
                    entry.getValue().remove(connection);
                }
            }
        }
    }

    public void broadcast(int gameID, String excludeClient, ServerMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (Map.Entry<Integer, Vector<Connection>> entry : connections.entrySet()){
            if (entry.getKey() == gameID){
                for (var connection : entry.getValue()){
                    if (connection.session.isOpen()){
                        if (!connection.authToken.equals(excludeClient)){
                            connection.send(notification.toString());
                        }
                    } else {
                        removeList.add(connection);
                    }
                }
            }
        }
        for (var c : removeList){
            remove(c.authToken);
        }
    }

    public void broadCastTo(int gameID, String client, ServerMessage notification) throws IOException {
        for (var connection : connections.get(gameID)){
            if (connection.authToken.equals(client)){
                connection.send(notification.toString());
            }
        }
    }

    public void broadCastError(String authToken, ErrorMessage errorMessage, Session session) throws IOException {
        var connection = new Connection(authToken, null, session);
        connection.send(errorMessage.toString());
    }
}
