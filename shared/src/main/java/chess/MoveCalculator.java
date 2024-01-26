package chess;

import java.util.Collection;

public interface MoveCalculator {

    Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition);
}
