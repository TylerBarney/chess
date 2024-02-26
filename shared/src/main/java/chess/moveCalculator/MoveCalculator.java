package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public interface MoveCalculator {

    Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition);
}
