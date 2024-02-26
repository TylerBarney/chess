package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

abstract public class MoveCalculator {

    abstract Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition);

    int canMove(ChessBoard board, ChessPosition endPosition, ChessGame.TeamColor teamColor){
        if (endPosition.getRow() <= 0 || endPosition.getRow() >= 9) return 3;
        if (endPosition.getColumn() <= 0 || endPosition.getColumn() >= 9) return 3;
        if (board.getPiece(endPosition) == null) return 0;
        if (board.getPiece(endPosition).getTeamColor() == teamColor) return 2;
        if (board.getPiece(endPosition).getTeamColor() != teamColor) return 1;
        return 0;
    }
}
