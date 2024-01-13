package chess;

import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //Array of ChessPieces. Their position in the array is their position on the board
    ChessPiece[][] unCapturedPieces;
    public ChessBoard() {
        //intialize array on build
        unCapturedPieces = new ChessPiece[8][8];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //add piece to board array
        unCapturedPieces[position.getRow()-1][position.getColumn()-1] = piece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        //return piece in position
        return unCapturedPieces[position.getRow()-1][position.getColumn()-1];
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {


        throw new RuntimeException("Not implemented");
    }

    //checks if position is empty
    public boolean isPlaceEmpty(ChessPosition endPosition){
        if (this.getPiece(endPosition) == null) return true;
        return false;
    }
}
