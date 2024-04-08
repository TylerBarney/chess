package model.webSocketMessages;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public ChessMove move;
    public int gameID;
    public MakeMoveCommand(String authToken, int gameID, ChessMove move) {
        super(authToken);
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
    }
}
