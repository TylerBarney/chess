package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator implements MoveCalculator{

    private ChessPiece thisPiece;
    public BishopMovesCalculator(ChessPiece thisPiece){
        this.thisPiece = thisPiece;
    }
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<>();
        ChessPosition endPosition;
        int i;
        //up right
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
        while (canMove(board, endPosition) != 3 && canMove(board, endPosition) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
        }
        //down right
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
        while (canMove(board, endPosition) != 3 && canMove(board, endPosition) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
        }
        //up left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
        while (canMove(board, endPosition) != 3 && canMove(board, endPosition) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
        }
        //down left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
        while (canMove(board, endPosition) != 3 && canMove(board, endPosition) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
        }
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
