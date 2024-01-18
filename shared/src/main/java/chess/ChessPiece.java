package chess;

import java.lang.reflect.Array;
import java.util.*;

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
            return this.bishopMoves(myPosition, board);
        } else if (type == PieceType.ROOK){
            return this.rookMoves(myPosition, board);
        } else if (type == PieceType.QUEEN){
            //Queen can move like both a rook and bishop
            possibleMoves.addAll(this.bishopMoves(myPosition, board));
            possibleMoves.addAll(this.rookMoves(myPosition, board));
        } else if (type == PieceType.KING){
            return this.kingMoves(myPosition, board);
        } else if (type == PieceType.PAWN){
            return this.pawnMoves(myPosition, board);
        } else if (type == PieceType.KNIGHT){
            return this.knightMoves(myPosition, board);
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

    //calculates bishop moves
    public Collection<ChessMove> bishopMoves(ChessPosition myPosition, ChessBoard board){
        int i = 1;
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        Set<Integer> values = Set.of(0,9);
        ChessPosition endPosition;
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
        return possibleMoves;
    }

    public Collection<ChessMove> rookMoves(ChessPosition myPosition, ChessBoard board){
        int i = 1;
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        Set<Integer> values = Set.of(0,9);
        ChessPosition endPosition;
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
        return possibleMoves;
    }

    public Collection<ChessMove> kingMoves(ChessPosition myPosition, ChessBoard board){
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition endPosition;

        //up
        if (myPosition.getRow() != 8){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //down
        if (myPosition.getRow() != 1){
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //left
        if (myPosition.getColumn() != 1){
            endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //right
        if (myPosition.getColumn() != 8){
            endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //up -right
        if (myPosition.getRow() != 8 && myPosition.getColumn() != 8){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //up-left
        if (myPosition.getRow() != 8 && myPosition.getColumn() != 1){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //down-right
        if (myPosition.getRow() != 1 && myPosition.getColumn() != 8){
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        //down-left
        if (myPosition.getRow() != 1 && myPosition.getColumn() != 1){
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
            if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            else{
                if (this.canTakePiece(endPosition, board)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
        return possibleMoves;
    }

    //pawn moves
    public Collection<ChessMove> pawnMoves(ChessPosition myPosition, ChessBoard board){
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition endPosition;

        //move forward if no obstructing piece
        if (this.pieceColor == ChessGame.TeamColor.WHITE){
            endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
            if (board.isPlaceEmpty(endPosition)) {
                if (!checkPromotion(endPosition)){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
                }
            }
            //checks for initial move, moving 2 spaces forward
            if (myPosition.getRow() == 2){
                ChessPosition doubleEndPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                endPosition = new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn());
                if (board.isPlaceEmpty(endPosition) && board.isPlaceEmpty(doubleEndPosition)) {
                    if (!checkPromotion(endPosition)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else {
                        possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
                    }
                }
            }
        } else {
            endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
            if (board.isPlaceEmpty(endPosition)) {
                if (!checkPromotion(endPosition)){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                } else {
                    possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
                }
            }
            //checks if its in starting place and can move 2 forward
            if (myPosition.getRow() == 7){
                ChessPosition doubleEndPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                endPosition = new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn());
                if (board.isPlaceEmpty(endPosition) && board.isPlaceEmpty(doubleEndPosition)) {
                    if (!checkPromotion(endPosition)){
                        possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else {
                        possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
                    }
                }
            }
        }

        //capture top right piece
        endPosition = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
        if (!board.isPlaceEmpty(endPosition) && this.canTakePiece(endPosition, board)){
            if (!checkPromotion(endPosition)){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else {
                possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
            }
        }

        //capture top left piece
        endPosition = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
        if (!board.isPlaceEmpty(endPosition) && this.canTakePiece(endPosition, board)){
            if (!checkPromotion(endPosition)){
                possibleMoves.add(new ChessMove(myPosition, endPosition, null));
            } else {
                possibleMoves.add(new ChessMove(myPosition, endPosition, this.type));
            }
        }

        return possibleMoves;
    }

    public boolean checkPromotion(ChessPosition endPosition){

        return false;
    }

    public Collection<ChessMove> knightMoves(ChessPosition myPosition, ChessBoard board){
        //one up two right

        //one up two left

        //down one two right

        //one down two left

        //two up one left

        //two up one right

        //two down one left

        //two down one right

        int[][] moveSet = { {1,2}, {1,-2}, {-1,2}, {-1, -2}, {2, -1}, {2, 1}, {-2, -1}, {-2, 1}};
        HashSet<ChessMove> possibleMoves = new HashSet<>();
        ChessPosition endPosition;
        for (int i = 0; i < moveSet.length; i++){
            endPosition = new ChessPosition(myPosition.getRow() + moveSet[i][0], myPosition.getColumn()  + moveSet[i][1]);
            if (this.inBounds(endPosition)){
                if (board.isPlaceEmpty(endPosition)) possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                else if (board.getPiece(endPosition).getTeamColor() != this.pieceColor){
                    possibleMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
        return possibleMoves;
    }

    public boolean inBounds(ChessPosition position){
        if (position.getRow() <= 0 || position.getRow() >= 9) return false;
        else if (position.getColumn() <= 0 || position.getColumn() >= 9) return false;
        return true;
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
