package chess.moveCalculator;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator extends MoveCalculator{
    private ChessPiece thisPiece;
    public RookMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }

    @Override
    public Collection<ChessMove> pieceMoves (ChessBoard board, ChessPosition myPosition){
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition;
        int i;
        //up right
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn());
        }
        //down right
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn());
        }
        //up left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() - i);
        }
        //down left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn() + i);
        }

        if (!thisPiece.getHasMoved()){
            //get king, check if it has moved
            ChessPosition kingPosition = (thisPiece.getTeamColor() == ChessGame.TeamColor.WHITE) ? new ChessPosition(1, 5) : new ChessPosition(8, 5);
            ChessPiece king = board.getPiece(kingPosition);

            if (king != null && king.getPieceType() == ChessPiece.PieceType.KING){
                if (!king.getHasMoved()){
                    if (myPosition.getColumn() == 8){ //kingside castle
                        //check pieces between
                        if (!board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 6)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 7))){
                            return moves;
                        }
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), 6), null));
                    } else if (myPosition.getColumn() == 1){ //queen-side castle
                        //check pieces between
                        if (!board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 4)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 3)) || !board.isPlaceEmpty(new ChessPosition(myPosition.getRow(), 2))){
                            return moves;
                        }
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow(), 4), null));
                    }
                }
            }

        }
        return moves;
    }
}
