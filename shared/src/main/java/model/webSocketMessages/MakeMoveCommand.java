package model.webSocketMessages;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private ChessMove move;
    public MakeMoveCommand(String authToken, ChessMove move) {
        super(authToken);
        this.move = move;
        commandType = CommandType.MAKE_MOVE;
    }
}
