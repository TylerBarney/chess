package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.HashSet;

abstract public class KingKnightSharedCalculator extends MoveCalculator{
    public void standardMoves(ChessPosition myPosition, int[][] possibleMoves, HashSet<ChessMove> moves, ChessBoard board, ChessGame.TeamColor teamColor){
        ChessPosition endPosition;
        for (int i = 0; i < possibleMoves.length; i++){
            endPosition = new ChessPosition(myPosition.getRow() + possibleMoves[i][0], myPosition.getColumn() + possibleMoves[i][1]);
            if (canMove(board, endPosition, teamColor) == 0 || canMove(board, endPosition, teamColor) == 1){
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
    }
}
