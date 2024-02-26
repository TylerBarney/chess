package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator extends KingKnightSharedCalculator{
    private ChessPiece thisPiece;
    public KnightMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        int[][] possibleMoves = {{2,-1}, {2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2}};
        standardMoves(myPosition, possibleMoves, moves, board, thisPiece.getTeamColor());
        return moves;
    }

}
