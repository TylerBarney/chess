package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

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
        if (type == PieceType.BISHOP) {
            int i = 1;
            //iterate through each space in the top right direction and checks for valid moves
            while (!values.contains(myPosition.getRow() + i) && !values.contains(myPosition.getColumn() + i)) {
                endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
                //if space is empty add to list
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    //checks if piece can be captured
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break; //stops checking further that direction
                }
            }
            i = 1;
            //iterate through each space in the top left direction and checks for valid moves
            while (!values.contains(myPosition.getRow() + i) && !values.contains(myPosition.getColumn() - i)) {
                endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
            i = 1;
            //iterate through each space in the bottom right direction and checks for valid moves
            while (!values.contains(myPosition.getRow() - i) && !values.contains(myPosition.getColumn() + i)) {
                endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
            i = 1;
            //iterate through each space in the bottom left direction and checks for valid moves
            while (!values.contains(myPosition.getRow() - i) && !values.contains(myPosition.getColumn() - i)) {
                endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
        } else if (type == PieceType.ROOK){
            int i = 1;
            //iterate through each space in the up direction and checks for valid moves
            while (!values.contains(myPosition.getRow() + i)) {
                endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
                //if space is empty add to list
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    //checks if piece can be captured
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break; //stops checking further that direction
                }
            }
            i = 1;
            //iterate through each space in the down direction and checks for valid moves
            while (!values.contains(myPosition.getRow() - i)) {
                endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
            i = 1;
            //iterate through each space in the right direction and checks for valid moves
            while (!values.contains(myPosition.getColumn() + i)) {
                endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
            i = 1;
            //iterate through each space in the left direction and checks for valid moves
            while (!values.contains(myPosition.getColumn() - i)) {
                endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
                if (board.isPlaceEmpty(endPosition)) {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    i++;
                } else{
                    if (this.canTakePiece(endPosition, board)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else break;
                }
            }
        }
        return possibleMoves;
        //throw new RuntimeException("Not implemented");
    }

    //checks position for enemy or team
    public boolean canTakePiece(ChessPosition endPosition, ChessBoard board){
        if (board.getPiece(endPosition) == null) return true;
        if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() == this.pieceColor) return false;
        if (board.getPiece(endPosition) != null && board.getPiece(endPosition).getTeamColor() != this.pieceColor) return true;
        return true;
    }
}
