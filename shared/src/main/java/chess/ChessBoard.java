package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

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

        //setting Piece has moved
        //check if has moved is false. create resetboard check if position is different on our board than resetBoard. if it is set to true
        if (piece != null && !piece.getHasMoved()){
            ChessBoard resetBoard = new ChessBoard();
            resetBoard.resetBoard();
            ChessPosition resetPosition = resetBoard.searchBoardFor(piece);
            resetBoard.addPiece(resetPosition, null);
            ChessPosition resetPosition2 = resetBoard.searchBoardFor(piece);
            if (!position.equals(resetPosition) && !position.equals(resetPosition2)){
                piece.setHasMoved(true);
            }
        }
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

        //white side
        //this.addPiece(new ChessPosition(1,1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        unCapturedPieces[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        unCapturedPieces[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        unCapturedPieces[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        unCapturedPieces[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        unCapturedPieces[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        unCapturedPieces[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        unCapturedPieces[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        unCapturedPieces[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < unCapturedPieces[0].length; i++){
            unCapturedPieces[1][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        //black side
        unCapturedPieces[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        unCapturedPieces[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        unCapturedPieces[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        unCapturedPieces[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        unCapturedPieces[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        unCapturedPieces[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        unCapturedPieces[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        unCapturedPieces[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 0; i < unCapturedPieces[0].length; i++){
            unCapturedPieces[6][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    //checks if position is empty
    public boolean isPlaceEmpty(ChessPosition endPosition){
        if (this.getPiece(endPosition) == null) return true;
        return false;
    }

    public ChessBoard copy() {
        ChessBoard copyBoard = new ChessBoard();
        for (int i = 0; i < this.unCapturedPieces.length; i++){
            for (int j = 0; j < this.unCapturedPieces.length; j++){
                if (unCapturedPieces[i][j] != null){
                    copyBoard.unCapturedPieces[i][j] = unCapturedPieces[i][j].copy();
                }

            }
        }
        return copyBoard;
    }

    //searches board for a piece. Returns position or null
    public ChessPosition searchBoardFor(ChessPiece piece){
        ChessPosition iteratorPosition;
        ChessPiece currentPiece;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++){
                iteratorPosition = new ChessPosition(i, j);
                currentPiece = this.getPiece(iteratorPosition);
                if (currentPiece != null && currentPiece.equals(piece)) return iteratorPosition;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(unCapturedPieces, that.unCapturedPieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(unCapturedPieces);
    }

    @Override
    public String toString() {
        String outString = "ChessBoard{";
        for (int i = 0; i < unCapturedPieces.length; i++){
            for (int j = 0; j < unCapturedPieces.length; j++){
                if (unCapturedPieces[i][j] != null) {
                    outString = outString + "Position " + i + ", " + j + " ";
                    outString = outString + unCapturedPieces[i][j].toString() + "\n";
                }
            }
        }
        return outString;
//        return "ChessBoard{" +
//                "unCapturedPieces=" + Arrays.toString(unCapturedPieces) +
//                '}';
    }
}
