package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends MoveCalculator{
    private ChessPiece thisPiece;
    public KnightMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        int[][] possibleMoves = {{2,-1}, {2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2}};
        ChessPosition endPosition;
        for (int i = 0; i < possibleMoves.length; i++){
            endPosition = new ChessPosition(myPosition.getRow() + possibleMoves[i][0], myPosition.getColumn() + possibleMoves[i][1]);
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 0 || canMove(board, endPosition, thisPiece.getTeamColor()) == 1){
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return moves;
    }

}
