package chess;

import java.lang.reflect.Array;
import java.util.*;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        Set<Integer> values = Set.of(0,9);
        ChessPosition endPosition;

        //checks piece type and returns possible moves
        KingMovesCalculator kingMovesCalculator = new KingMovesCalculator(this);
        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator(this);
        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator(this);
        QueenMovesCalculator queenMovesCalculator = new QueenMovesCalculator(this);
        KnightMovesCalculator knightMovesCalculator = new KnightMovesCalculator(this);
        PawnMovesCalculator pawnMovesCalculator = new PawnMovesCalculator(this);
        if(type == PieceType.BISHOP) return bishopMovesCalculator.pieceMoves(board, myPosition);
        if(type == PieceType.ROOK) return rookMovesCalculator.pieceMoves(board, myPosition);
        if(type == PieceType.QUEEN) return queenMovesCalculator.pieceMoves(board, myPosition);
        if(type == PieceType.KING) return kingMovesCalculator.pieceMoves(board, myPosition);
        if(type == PieceType.KNIGHT) return knightMovesCalculator.pieceMoves(board, myPosition);
        if(type == PieceType.PAWN) return pawnMovesCalculator.pieceMoves(board, myPosition);
        return possibleMoves;
        //throw new RuntimeException("Not implemented");
    }
    public ChessPiece copy(){
        return new ChessPiece(this.pieceColor, this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
}
