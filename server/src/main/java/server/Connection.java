package server;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authToken;
    public ChessGame.TeamColor playerColor;
    public Session session;

    public Connection(String authToken, ChessGame.TeamColor playerColor, Session session){
        this.session = session;
        this.authToken = authToken;
        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

}
