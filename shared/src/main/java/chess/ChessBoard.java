package chess;

import java.util.ArrayList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //Arrays for uncapturedpieces and their positions
    ArrayList<ChessPiece> unCapturedPieces;
    ArrayList<ChessPosition> unCapturedPositions;
    public ChessBoard() {
        //intialize arrays on build
        unCapturedPieces = new ArrayList<>();
        unCapturedPositions = new ArrayList<>();
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //add piece to board arrays
        unCapturedPieces.add(piece);
        unCapturedPositions.add(position);
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
        //get index of piece in arrays
        int positionRow = position.getRow();
        int positionCol = position.getColumn();
        int positionIndex;
        for (positionIndex = 0; positionIndex < unCapturedPositions.size(); positionIndex++){
            if (positionRow == unCapturedPositions.get(positionIndex).getRow()){
                if (positionCol == unCapturedPositions.get(positionIndex).getColumn()) {
                    System.out.println("Found it!");
                    break;
                }
            }
        }
        //return piece based on index
        if (positionIndex >= 0) return unCapturedPieces.get(positionIndex);
        else return null;

        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {


        throw new RuntimeException("Not implemented");
    }
}
