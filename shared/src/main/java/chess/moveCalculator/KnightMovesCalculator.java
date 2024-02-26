package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements MoveCalculator{
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
            if (canMove(board, endPosition) == 0 || canMove(board, endPosition) == 1){
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return moves;
    }

    private int canMove(ChessBoard board, ChessPosition endPosition){
        if (endPosition.getRow() <= 0 || endPosition.getRow() >= 9) return 3;
        if (endPosition.getColumn() <= 0 || endPosition.getColumn() >= 9) return 3;
        if (board.getPiece(endPosition) == null) return 0;
        if (board.getPiece(endPosition).getTeamColor() == this.thisPiece.getTeamColor()) return 2;
        if (board.getPiece(endPosition).getTeamColor() != this.thisPiece.getTeamColor()) return 1;
        return 0;
    }
}
