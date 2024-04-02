package model.webSocketMessages;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    public int gameID;
    public ChessGame.TeamColor playerColor;
    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor playerColor) {
        super(authToken);
        commandType = CommandType.JOIN_PLAYER;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }
}
