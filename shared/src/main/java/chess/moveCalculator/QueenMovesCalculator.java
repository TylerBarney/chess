package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements MoveCalculator{
    private ChessPiece thisPiece;
    public QueenMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(thisPiece);
        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(thisPiece);
        HashSet<ChessMove> moves = new HashSet<>(bishopMovesCalculator.pieceMoves(board, myPosition));
        moves.addAll(rookMovesCalculator.pieceMoves(board, myPosition));
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
