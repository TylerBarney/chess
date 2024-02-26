package chess.moveCalculator;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public class BishopMovesCalculator extends MoveCalculator{

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
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() + i);
        }
        //down right
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() + i);
        }
        //up left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() + i, myPosition.getColumn() - i);
        }
        //down left
        i = 1;
        endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
        while (canMove(board, endPosition, thisPiece.getTeamColor()) != 3 && canMove(board, endPosition, thisPiece.getTeamColor()) != 2){
            moves.add(new ChessMove(myPosition, endPosition, null));
            if (canMove(board, endPosition, thisPiece.getTeamColor()) == 1) break;
            i++;
            endPosition = new ChessPosition(myPosition.getRow() - i, myPosition.getColumn() - i);
        }
        return moves;
    }
}
