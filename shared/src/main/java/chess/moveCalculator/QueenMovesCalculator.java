package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator extends MoveCalculator{
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

}
